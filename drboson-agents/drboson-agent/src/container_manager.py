import docker
from tempfile import NamedTemporaryFile
from jinja2 import Environment, FileSystemLoader
import templates
import pathlib
from pathlib import Path
from docker.errors import  BuildError


# Change this and add a workspace reference which contains everything
class ContainerManager:
    def __init__(self, config):
        self.client = docker.from_env()
        self.config = config

    def create_run_container(self, run, local_workdir, container_workdir, dataset_location, env_file):
        image_name = f"{run['id']}-image"
        volumes = {local_workdir: {'bind': str(container_workdir), 'mode': 'rw'}}

        self.__build_image(image_name, run, local_workdir, container_workdir, dataset_location, env_file)
        container = self.client.containers.run(image_name, volumes=volumes, detach=True)

        return container.id

    def __render_dockerfile(self, run, container_workdir, dataset_location, env_file):
        return templates.create_dockerfile(workdir=container_workdir, env_file_path=env_file,
                                           run_id=run['id'], project_id=run['project_id'],
                                           dataset_location=dataset_location)

    def __build_image(self, name, run, local_workdir, container_workdir, dataset_location, env_file):
        print(f"[-] Building the {name} image")
        with NamedTemporaryFile(dir=local_workdir) as dockerfile:
            dockerfile_name = Path(dockerfile.name).name
            dockerfile.write(self.__render_dockerfile(run, container_workdir, dataset_location, env_file))
            dockerfile.seek(0)

            try:
                self.client.images.build(path=str(local_workdir), dockerfile=dockerfile_name, custom_context=False, tag=name)
            except BuildError as e:
                for l in e.build_log:
                    print(l)

        print(f"[+] Image {name} built")

    # def consume_events(self):
    #     event_filter = {'type': 'container'}
    #
    #     for event in self.client.events(decode=True, filters=event_filter):
    #         print(event)

