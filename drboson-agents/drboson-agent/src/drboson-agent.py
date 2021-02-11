from container_manager import ContainerManager
from handlers import CommunicationHandler
from handlers import DatasetStorageHandler
from rdf_store import RDFStore
import consumers
import threading
from config import config


def main():
    container_manager = ContainerManager(config)
    communication_handler = CommunicationHandler()

    rdf_store_config = config['rdf_store']
    endpoint, username, password = rdf_store_config['endpoint'], \
                                   rdf_store_config['username'],\
                                   rdf_store_config['password']
    rdf_store = RDFStore(endpoint, username, password)
    dataset_storage_handler = DatasetStorageHandler(rdf_store)

    threading.Thread(target=consumers.run_consumer, args=(container_manager,)).start()
    threading.Thread(target=consumers.run_communication_consumer, args=(communication_handler,)).start()
    threading.Thread(target=consumers.dataset_storage_job_consumer, args=(dataset_storage_handler, )).start()
    # docker_event_consumer


if __name__ == '__main__':
    main()
