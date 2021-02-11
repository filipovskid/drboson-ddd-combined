FROM continuumio/miniconda3

WORKDIR {{workdir}}

COPY environment.yml .

RUN cat environment.yml

RUN conda env create -f environment.yml

SHELL ["conda", "run", "-n", "{{env_name}}", "/bin/bash", "-c"]

RUN conda install -c conda-forge python-confluent-kafka

ENV DRBOSON_BOOTSTRAP_SERVERS {{bootstrap_servers}}

ENV DRBOSON_PRODUCER_TOPIC {{communication_topic}}

ENV DRBOSON_RUN_ID {{run_id}}

ENV DRBOSON_PROJECT_ID {{project_id}}

ENV DRBOSON_WORKSPACE {{workdir}}

ENV DRBOSON_DATASET_LOCATION {{dataset_location}}

CMD python executor.py
