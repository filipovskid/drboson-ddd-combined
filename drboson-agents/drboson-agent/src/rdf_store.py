import stardog
import requests
import urllib.parse
from stardog.exceptions import StardogException

class RDFStore:
    def __init__(self, endpoint, username='admin', password='admin'):
        self.endpoint = endpoint
        self.username = username
        self.password = password

    def create_dataset_database(self, database_name, dataset_path):
        connection_details = {
            'endpoint': self.endpoint,
            'username': self.username,
            'password': self.password
        }

        with stardog.Admin(**connection_details) as admin:
            if database_name in [db.name for db in admin.databases()]:
                admin.database(database_name).drop()
            db = admin.new_database(database_name)

            with stardog.Connection(database_name, **connection_details) as conn:
                conn.begin()
                conn.add(stardog.content.File(str(dataset_path)))
                conn.commit()

                if conn.size(exact=True) <= 0:
                    admin.database(database_name).drop()
                    raise StardogException('No triples loaded!')

        return database_name

    def query_to_file(self, database_name, query, file_path, accept_type='text/csv'):
        data = {'query': query, 'baseURI': None, 'limit': None, 'offset': None, 'timeout': None, 'reasoning': None}
        headers = {'Accept': accept_type}
        url = urllib.parse.urljoin(self.endpoint, database_name) + "/query"

        with requests.post(url,
                           auth=(self.username, self.password),
                           data=data,
                           headers=headers,
                           stream=True) as r:
            r.raise_for_status()
            with open(file_path, 'wb') as f:
                for chunk in r.iter_content(chunk_size=None):
                    if chunk:
                        f.write(chunk)
