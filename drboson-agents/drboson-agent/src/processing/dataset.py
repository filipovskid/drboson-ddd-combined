from .schema_handling import get_schema_from_df, drboson_dask_type
import dask.dataframe as dd


class Dataset:
    """ This class represents a DRBoson dataset.
    This Database class allows you to:

    * Read a dataset as a Dask dataframe from a given datasource
    * Write a Dask dataframe to a dataset on a given data storage
    * Obtain the schima of a dataset
    * Edit the schema of a dataset
    """

    def __init__(self, dataset_id, project_id, schema=None):
        self.dataset_id = dataset_id
        self.project_id = project_id
        self.cols = None
        self.schema = schema

    def __check_sampling_arguments(self, sampling_method, max_records=None, ratio=None):
        if sampling_method == 'HEAD_SEQUENTIAL':
            if max_records is None:
                raise ValueError('max_records must not be empty when sampling sequentally')

        if sampling_method == 'RANDOM_RATIO':
            if ratio is None:
                raise ValueError('ratio must not be empty when using RANDOM_RATIO sampling method')

        if sampling_method == 'RANDOM_LIMIT':
            raise ValueError('RANDOM_LIMIT sampling method is currently not supported')

    def __sample_dataframe(self, df, sampling_method, max_records=None, ratio=None):
        sample_from_dataframe = {
            'HEAD_SEQUENTIAL': lambda data_frame: data_frame.loc[:(max_records - 1)],
            'RANDOM_RATIO': lambda data_frame: data_frame.sample(frac=ratio),
            'RANDOM_LIMIT': lambda data_frame: data_frame.sample(n=max_records)
        }

        self.__check_sampling_arguments(sampling_method, max_records, ratio)

        return sample_from_dataframe[sampling_method](df)

    def import_dataset(self, data_source,
                       sampling_method='HEAD_SEQUENTIAL',
                       max_records=10000,
                       ratio=None,
                       # infer_with_pandas=True,
                       # parse_dates=True,
                       # bool_as_str=False,
                       # float_precision=None
                       ):
        """Read an unprocessed dataset from the data source as a Dask dataframe, infer its schema,
        create a sample according to the input params and export this dataset in parquet format
        enabling it to be processable.

        :param data_source -- local filesystem path to the data
        :param sampling_method -- sampling method
            - "head" returns the first rows from the dataset
            - "random" returns random rows from the dataset
        :param max_records -- max number of rows returned
        :param ratio -- fractions of rows to be returned
        """

        # Temporary. Not a solution
        df = dd.read_csv(data_source)
        df_sample = self.__sample_dataframe(df, sampling_method, max_records, ratio)
        schema = get_schema_from_df(df)

        return df_sample, schema

    @staticmethod
    def __get_dataframe_column_stats(df, schema):
        """
        Computes dataframe column statistics based on columns within the schema.
        Statistics include:
            - missing values
            - OK values

        :param df: Dask DataFrame
        :param schema: data schemas
        :return: dictionary in the form { 'column_name': stats -> { 'noMissingValues', 'noOK' } }
        """

        row_count = df.shape[0].compute()
        missing_values = df.isna().sum().compute()
        stats = {}

        for column in schema:
            column_name = column['name']
            column_missing_values = int(missing_values[column_name])

            stats[column_name] = {
                'noMissingValues': column_missing_values,
                'noOK': int(row_count - column_missing_values)
            }

        return stats

    @staticmethod
    def get_filtered_schema_parts(schema, columns=None, bool_as_str=False):
        names = []
        dtypes = {}

        for column in schema:
            name = column['name']
            col_type = column['type']

            names.append(name)
            dtypes[name] = drboson_dask_type(col_type)

        if columns is not None:
            columns = list(set(columns))
            names = columns
            dtypes = {dtypes[column] for column in dtypes if column in columns}

        return names, dtypes

    @staticmethod
    def export_dataframe(df, schema, data_storage, columns=None, bool_as_str=False):
        columns, dtypes = Dataset.get_filtered_schema_parts(schema=schema, columns=columns, bool_as_str=bool_as_str)
        df = df.astype(dtypes)
        df.to_parquet(data_storage, engine='pyarrow')

    @staticmethod
    def compose_column_info(df, schema):
        """
        Prepares per column information which should be dispatched to the backend

        :param df: Dask DataFrame
        :param schema: data schema
        :return: list of column information in the format
                { 'name': column-name, 'type': column-type, 'noMissingValues': missing-count, 'noOK': ok-count }
        """

        data = []
        column_stats = Dataset.__get_dataframe_column_stats(df=df, schema=schema)

        for column in schema:
            column_data = dict(column, **column_stats[column['name']])
            data.append(column_data)

        return data
