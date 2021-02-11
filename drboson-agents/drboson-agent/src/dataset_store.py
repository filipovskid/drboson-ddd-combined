import sys

import messages
import producers
import schemas

class DatasetStore:
    def __init__(self, topic):
        self.producer = producers.avro_messages_producer(schemas.dataset_record_schema)
        self.topic = topic

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

    def create_dataset(self, dataset_id, project_id, name, description, location, mimetype, dataset_type):
        message = messages.create_dataset_message(dataset_id=dataset_id,
                                                  project_id=project_id,
                                                  name=name,
                                                  description=description,
                                                  location=location,
                                                  mimetype=mimetype,
                                                  dataset_type=dataset_type)

        self.__produce(message=message, key=dataset_id, on_delivery=DatasetStore.__delivery_callback)
