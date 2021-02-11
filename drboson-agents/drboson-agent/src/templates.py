from jinja2 import Environment, FileSystemLoader
from config import config
import yaml


def create_dockerfile(env_file_path, **kwargs):
    dockerfiles_dir = config['docker']['dockerfiles_dir']
    dockerfile_name = config['docker']['dockerfile_name']
    bootstrap_server = config['kafka']['servers']
    producer_topic = config['kafka']['communication-topic']

    def extract_env_name(env_file):
        with open(env_file, 'r') as stream:
            try:
                env_config = yaml.safe_load(stream)
                if 'name' not in env_config:
                    pass

                return env_config['name']
            except yaml.YAMLError as exc:
                print(exc)

    env = Environment(loader=FileSystemLoader(dockerfiles_dir))
    dockerfile_template = env.get_template(dockerfile_name)

    return dockerfile_template.render(communication_topic=producer_topic,
                                      bootstrap_servers=bootstrap_server,
                                      env_name=extract_env_name(env_file_path),
                                      **kwargs).encode('utf-8')
