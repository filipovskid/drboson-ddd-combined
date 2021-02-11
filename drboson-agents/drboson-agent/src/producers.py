from confluent_kafka import Producer
from confluent_kafka import SerializingProducer
from confluent_kafka.serialization import StringSerializer
from confluent_kafka.schema_registry.avro import AvroSerializer
from confluent_kafka.schema_registry import SchemaRegistryClient
import socket
from config import config


def json_messages_producer():
    conf = {'bootstrap.servers': config['kafka']['servers'],
            'client.id': socket.gethostname(),
            'retries': 10,
            'retry.backoff.ms': 1000,
            'queue.buffering.max.ms': 100}

    return Producer(conf)


def avro_messages_producer(schema):
    schema_registry_conf = {'url': config['kafka']['schema_registry']}
    schema_registry_client = SchemaRegistryClient(schema_registry_conf)

    avro_serializer = AvroSerializer(schema, schema_registry_client)
    string_serializer = StringSerializer('utf-8')

    producer_conf = {'bootstrap.servers': config['kafka']['servers'],
                     'key.serializer': string_serializer,
                     'value.serializer': avro_serializer}

    return SerializingProducer(producer_conf)