import sys
sys.path.append('./drboson')
from drboson.drboson import DRBoson
from drboson.drboson import Run
from drboson.DRBosonProducer import RemoteProducer
import socket
import os


def main():

    bootstrap_servers = os.environ.get('DRBOSON_BOOTSTRAP_SERVERS')
    producer_topic = os.environ.get('DRBOSON_PRODUCER_TOPIC')
    run_id = os.environ.get('DRBOSON_RUN_ID')
    project_id = os.environ.get('DRBOSON_PROJECT_ID')
    workspace_dir = os.environ.get('DRBOSON_WORKSPACE')
    dataset_location = os.environ.get('DRBOSON_DATASET_LOCATION')

    _run = Run(run_id=run_id,
              project_id=project_id,
              work_dir=workspace_dir,
              dataset_location=dataset_location)

    conf = {
        'bootstrap.servers': bootstrap_servers,
        'client.id': socket.gethostname(),
        'retries': 10,
        'retry.backoff.ms': 1000,
        'queue.buffering.max.ms': 100
    }

    producer = RemoteProducer(conf, topic=producer_topic)
    drboson = DRBoson(run=_run, producer=producer)
    drboson.started()

    try:
        import run
        run.run(drboson, dataset_location)
    except Exception as e:
        print(e)
        drboson.failed()
        return

    drboson.completed()

    # run.run()


if __name__ == '__main__':
    main()
