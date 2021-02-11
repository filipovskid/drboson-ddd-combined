from file_management import prepare_workspace, prepare_dataset, prepare_code, upload_file_to_s3
from job_management import JobStatusHandler
from rdf_transformation import RDFTransformer
from dataset_store import DatasetStore
from processing.dataset import Dataset
import file_management
from config import config
import producers
import pathlib
import messages
import schemas
import uuid
import copy
import json
import sys


def __run_setup(run_item):
    workspaces_path = pathlib.Path(config['workspace']['dir'])
    dataset_dir = config['workspace']['dataset']['dir']
    data_dir = config['workspace']['data']
    local_workdir_path, dataset_path, data_path = \
        prepare_workspace(workspaces_path, dataset_dir, data_dir, run_name=run_item['id'])

    datasets_bucket = config['buckets']['dataset']
    dataset_key = run_item['dataset_location']
    run_dataset_location = prepare_dataset(datasets_bucket, dataset_key, dataset_path)

    repo_url = run_item['repository']
    executor_path = pathlib.Path(config['exec']['executor'])
    prepare_code(repo_url, workdir_path=local_workdir_path, executor_path=executor_path)

    environment_file = local_workdir_path.joinpath(config['exec']['env_file'])
    container_workdir_path = pathlib.Path(config['workspace']['container'])
    container_dataset_location = container_workdir_path.joinpath(run_dataset_location.relative_to(local_workdir_path))

    if environment_file.exists() is False:
        pass

    return local_workdir_path, dataset_path, data_path, container_workdir_path, environment_file, container_dataset_location


def handle_run_execution(container_manager, run_item):
    local_workdir_path, dataset_path, data_path, container_workdir_path, env_file, container_dataset_location = \
        __run_setup(run_item)
    safe_run = copy.deepcopy(run_item)

    container_manager.create_run_container(run=safe_run,
                                           local_workdir=local_workdir_path,
                                           container_workdir=container_workdir_path,
                                           dataset_location=container_dataset_location,
                                           env_file=env_file)


# {
#     'id': 'run-id',
#     'type': 'type',
#     'payload': 'status | filename | log'
# }


class CommunicationHandler:
    def __init__(self):
        self.producer = producers.json_messages_producer()
        self.status_producer = producers.avro_messages_producer(schemas.status_record_schema)
        self.log_producer = producers.avro_messages_producer(schemas.log_record_schema)
        self.file_producer = producers.avro_messages_producer(schemas.file_record_schema)

    @staticmethod
    def __delivery_callback(err, msg):
        if err:
            sys.stderr.write('%% Message failed delivery: %s\n' % err)
        else:
            sys.stderr.write('%% Message delivered to %s [%d] @ %d\n' %
                             (msg.topic(), msg.partition(), msg.offset()))

    @staticmethod
    def __navigate_communication(producer, topic, message, **kwargs):
        try:
            producer.produce(topic, value=message, **kwargs)
            producer.poll(1)
        except BufferError as e:
            producer.poll(10)
            producer.produce(topic, value=message, **kwargs)

    def handle_status_change(self, run_id, project_id, payload):
        run_status = messages.create_status_message(run_id=run_id, project_id=project_id, status=payload)
        topic = config['kafka']['statuses-topic']

        self.__navigate_communication(producer=self.status_producer, topic=topic, message=run_status, key=run_id,
                                      on_delivery=CommunicationHandler.__delivery_callback)

    def handle_metric_logs(self, run_id, project_id, payload):
        metric_log = messages.create_log_message(run_id=run_id, project_id=project_id, log=payload)
        log_key = str(uuid.uuid4())  # Necessary for ksqldb table
        topic = config['kafka']['logs-topic']

        self.__navigate_communication(producer=self.log_producer, topic=topic, message=metric_log, key=log_key,
                                      on_delivery=CommunicationHandler.__delivery_callback)

    def handle_file_creation(self, run_id, project_id, payload):
        topic = config['kafka']['files-topic']
        files_bucket = config['buckets']['files']
        container_workspace = pathlib.Path(config['workspace']['container'])
        run_workspaces = pathlib.Path(config['workspace']['dir'])
        run_workspace_path = run_workspaces.joinpath(run_id)
        container_file_path = pathlib.Path(payload)
        file_path = run_workspace_path.joinpath(container_file_path.relative_to(container_workspace))

        if not run_workspace_path.exists():
            print(f'{run_id} Run workspace: {run_workspace_path} does not exist')

        if not file_path.exists():
            print(f'[{run_id}] Run file: {file_path} does not exist')

        if not file_path.is_file():
            print(f'[{run_id}] {file_path} is not a file')

        s3_file_name = f'{uuid.uuid4()}--{file_path.name}'

        uploaded = upload_file_to_s3(file_path, files_bucket, s3_file_name)

        if not uploaded:
            print(f'[{run_id}] {file_path} upload failed')
            return

        file_creation_message = messages.create_file_message(run_id=run_id, project_id=project_id,
                                                             file_id=str(uuid.uuid4()),
                                                             file_name=file_path.name, file_key=s3_file_name)
        self.__navigate_communication(producer=self.file_producer, topic=topic, message=file_creation_message,
                                      on_delivery=CommunicationHandler.__delivery_callback)

    def handle_run_communication(self, message_bytes):
        type_handlers = {
            'status': self.handle_status_change,
            'log': self.handle_metric_logs,
            'file': self.handle_file_creation
        }

        message = json.loads(message_bytes.decode('utf-8'))

        if 'type' in message and message['type'] in type_handlers:
            type_handlers[message['type']](run_id=message['run_id'],
                                           project_id=message['project_id'],
                                           payload=message['payload'])


class DatasetStorageHandler:
    def __init__(self, rdf_store):
        self.datasets_path = pathlib.Path(config['datasets']['dir'])
        self.datasets_bucket = config['buckets']['dataset']
        self.rdf_store = rdf_store
        self.dataset_store = DatasetStore(topic=config['kafka']['datasets-topic'])

    @staticmethod
    def __rdf_store_db_name(dataset_id):
        return 'drboson_' + dataset_id.replace('-', '_')

    @staticmethod
    def __rdf_result_dataset_name(dataset_id):
        return 'drboson_query_' + dataset_id.replace('-', '_')

    def local_storage(self, storage_job, status_handler):
        dataset_location = storage_job['dataset_location']
        dataset_id = storage_job['dataset_id']
        project_id = storage_job['project_id']
        dataset_path, dataset_samples, dataset_dump = \
            file_management.prepare_dataset_workspace(storage_job, self.datasets_path, dataset_location)

        file_management.download_file_from_s3(dataset_path, self.datasets_bucket, dataset_location)
        sample_name = str(uuid.uuid4())
        sample_path = dataset_samples.joinpath(sample_name)
        storage_path = dataset_path.joinpath(dataset_location)

        dataset = Dataset(dataset_id=dataset_id, project_id=project_id)
        df_sample, schema = dataset.import_dataset(data_source=dataset_path)
        Dataset.export_dataframe(df_sample, schema, data_storage=sample_path)
        column_data = Dataset.compose_column_info(df_sample, schema)

        status_handler.completed(storage_location=str(storage_path), sample_name=sample_name, column_data=column_data)

    def triple_store_storage(self, storage_job, status_handler):
        dataset_location = storage_job['dataset_location']
        dataset_id = storage_job['dataset_id']
        dataset_path = file_management.prepare_dataset_workspace(storage_job, self.datasets_path, dataset_location)

        file_management.download_file_from_s3(dataset_path, self.datasets_bucket, dataset_location)
        db_name = DatasetStorageHandler.__rdf_store_db_name(dataset_id)
        database_name = self.rdf_store.create_dataset_database(database_name=db_name,
                                                               dataset_path=dataset_path)

        # To avoid passing parameters consider using state within JobStatus
        status_handler.completed(storage_location=database_name)

    def rdf_transform_storage(self, storage_job, status_handler):
        dataset_location = storage_job['dataset_location']
        filename = dataset_location + '.csv'

        dataset_path = file_management.prepare_dataset_workspace(storage_job,
                                                                 self.datasets_path,
                                                                 filename=filename)
        payload = json.loads(storage_job['payload'])
        transformer = RDFTransformer(node=payload['node'],
                                     attributes=payload['attributes'],
                                     literals=payload['literals'],
                                     branches=payload['branches'])
        query = transformer.build_query()

        self.rdf_store.query_to_file(database_name=dataset_location,
                                     query=query,
                                     file_path=dataset_path,
                                     accept_type='text/csv')

        dataset_id = str(uuid.uuid4())
        project_id = storage_job['project_id']
        object_name = DatasetStorageHandler.__rdf_result_dataset_name(dataset_id)
        file_management.upload_file_to_s3(dataset_path, self.datasets_bucket, object_name)

        dataset_name = storage_job['dataset_name'] + '.csv'
        self.dataset_store.create_dataset(dataset_id=dataset_id,
                                          project_id=project_id,
                                          name=dataset_name,
                                          description="[DRBoson] Generated dataset.",
                                          location=object_name,
                                          mimetype='text/csv',
                                          dataset_type="COMMON")
        status_handler.completed()

    def handle_dataset_storage(self, dataset_storage_job):
        storage_types = {
            'STORAGE': self.local_storage,
            'RDF_READY_STORAGE': self.triple_store_storage,
            'RDF_TRANSFORM_STORAGE': self.rdf_transform_storage
        }

        if 'job_type' in dataset_storage_job and \
                dataset_storage_job['job_type'] in storage_types:
            storage_type = dataset_storage_job['job_type']

            status_handler = JobStatusHandler(topic=config['kafka']['job-status-topic'],
                                              job_id=dataset_storage_job['job_id'],
                                              job_type=dataset_storage_job['job_type'],
                                              dataset_id=dataset_storage_job['dataset_id'])
            try:
                storage_types[storage_type](dataset_storage_job, status_handler)
            except Exception as e:
                status_handler.failed(str(e))
