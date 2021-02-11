import numpy as np


drboson_dask_types_map = {
    'int': np.int32,
    'bigint': np.int64,
    'float': np.float32,
    'double': np.float64,
    'boolean': np.bool
}

dask_drboson_types_map = {
    'int64': 'bigint',
    'float64': 'double',
    'float32': 'float',
    'int32': 'int',
    'object': 'string',
    'int': 'int',
    'float': 'float',
    'bool': 'boolean',
    'datetime64[ns]': 'date',
    'Int64': 'bigint',
    'boolean': 'boolean'
}


def dask_drboson_type(dtype):
    return dask_drboson_types_map.get(str(dtype), "string")

def drboson_dask_type(drboson_type):
    return drboson_dask_types_map.get(str(drboson_type), np.object_)

def get_schema_from_df(df):
    """ Obtain dataset schema from a Dask DataFrame"""
    schema = []

    if len(set(df.columns)) != len(list(df.columns)):
        raise Exception("Dataframes containing multiple columns with the same name are not supported.")

    for col_name in df.columns:

        column_type = {
            'name': col_name,
            'type': dask_drboson_type(df[col_name].dtype)
        }
        schema.append(column_type)

    return Schema(schema)


class Schema(list):
    def __init__(self, data):
        list.__init__(self, data)

