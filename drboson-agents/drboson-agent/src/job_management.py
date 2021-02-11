from enum import Enum, auto
import messages
import producers
import schemas
import sys
import json


class JobStatus(Enum):
    PENDING = auto()
    RUNNING = auto()
    COMPLETED = auto()
    FAILED = auto()


class JobStatusHandler:
    record_schemas = {
        'STORAGE': schemas.storage_job_status_schema,
        'RDF_READY_STORAGE': schemas.rdf_ready_storage_job_status_schema,
    }

    def __init__(self, topic, job_id, job_type, dataset_id):
        print(json.loads(JobStatusHandler.record_schemas[job_type]))
        self.producer = producers.avro_messages_producer(JobStatusHandler.record_schemas[job_type])
        self.topic = topic
        self.job_id = job_id
        self.job_type = job_type
        self.dataset_id = dataset_id

    @staticmethod
    def __delivery_callback(err, msg):
        if err:
            sys.stderr.write('%% Message failed delivery: %s\n' % err)
        else:
            sys.stderr.write('%% Message delivered to %s [%d] @ %d\n' %
                             (msg.topic(), msg.partition(), msg.offset()))

    def __produce(self, message, **kwargs):
        try:
            self.producer.produce(self.topic, value=message, **kwargs)
            self.producer.poll(1)
        except BufferError as e:
            self.producer.poll(10)
            self.producer.produce(self.topic, value=message, **kwargs)

    def __communication_handler(self, status, message, **job_state):
        message = messages.create_job_status_message(job_id=self.job_id,
                                                     job_type=self.job_type,
                                                     dataset_id=self.dataset_id,
                                                     status=status.name,
                                                     message=message,
                                                     **job_state)
        print(message)

        self.__produce(message=message, key=self.job_id, on_delivery=JobStatusHandler.__delivery_callback)

    def failed(self, message='', **job_state):
        self.__communication_handler(JobStatus.FAILED, message, **job_state)

    def completed(self, message='', **job_state):
        self.__communication_handler(JobStatus.COMPLETED, message, **job_state)

    def pending(self, message='', **job_state):
        self.__communication_handler(JobStatus.PENDING, message, **job_state)

    def running(self, message='', **job_state):
        self.__communication_handler(JobStatus.RUNNING, message, **job_state)

