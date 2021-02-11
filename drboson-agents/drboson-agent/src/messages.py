import json


def create_status_change_json_message(run_id, status):
    message = {
        'id': run_id,
        'status': status.upper()
    }

    return json.dumps(message)


def create_log_json_message(run_id, log):
    message = {
        'id': run_id,
        'log': log
    }

    return json.dumps(message)


def create_file_creation_json_message(run_id, file_key):
    message = {
        'id': run_id,
        'file_key': file_key
    }

    return json.dumps(message)


def create_status_message(run_id, project_id, status):
    message = {
        'run_id': run_id,
        'project_id': project_id,
        'status': status.upper()
    }

    return message


def create_log_message(run_id, project_id, log):
    message = {
        'run_id': run_id,
        'project_id': project_id,
        'log': json.dumps(log)
    }

    return message


def create_file_message(run_id, project_id, file_id, file_name, file_key):
    message = {
        'run_id': run_id,
        'project_id': project_id,
        'file_id': file_id,
        'file_name': file_name,
        'file_key': file_key
    }

    return message


def create_job_status_message(job_id, job_type, dataset_id, status, message, **job_state):
    message = {
        'job_id': job_id,
        'job_type': job_type,
        'dataset_id': dataset_id,
        'status': status,
        'job_state': job_state.copy(),
        'message': message
    }

    return message


def create_dataset_message(dataset_id, project_id, name, description, location, mimetype, dataset_type):
    message = {
        'dataset_id': dataset_id,
        'project_id': project_id,
        'name': name,
        'description': description,
        'location': location,
        'mimetype': mimetype,
        'dataset_type': dataset_type
    }

    return message
