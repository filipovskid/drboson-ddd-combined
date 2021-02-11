config = {
    'buckets': {
        'dataset': 'dataset-filipovski',
        'files': 'files-filipovski',
        'logs': 'logs-filipovski'
    },
    'workspace': {
        'dir': '/Users/darko/drboson/run-workspaces',
        'dataset': {
            'dir': 'dataset'
        },
        'data': 'data',
        'container': '/drboson/workdir'
    },
    'docker': {
        'dockerfiles_dir': '/Users/darko/Documents/Projects/DRBoson/drboson-agent/',
        'dockerfile_name': 'Dockerfile'
    },
    'kafka': {
        'servers': '192.168.1.101',
        'runs-topic': 'runs',
        'statuses-topic': 'run_statuses',
        'logs-topic': 'run_logs',
        'files-topic': 'run_files',
        'datasets-topic': 'transform_datasets',
        'dataset-storage-topic': 'dataset_storage_jobs',
        'job-status-topic': 'job_status',
        'communication-topic': 'run_messages',
        'schema_registry': 'http://192.168.1.101:8081'
    },
    'exec': {
        'executor': '/Users/darko/Documents/Projects/DRBoson/drboson-agent/executor.py',
        'env_file': 'environment.yml'
    },
    'datasets': {
        'dir': '/Users/darko/drboson/dataset-workspaces',
    },
    'rdf_store': {
        'endpoint': 'http://192.168.1.100:5820',
        'username': 'admin',
        'password': 'admin'
    }
}