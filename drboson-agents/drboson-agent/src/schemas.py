run_record_schema = """
{
  "namespace": "com.filipovski.drboson.sharedkernel.integration.avro",
  "type": "record",
  "name": "RunStartedRecord",
  "fields": [
    { "name": "id", "type":  "string" },
    { "name": "project_id", "type": "string" },
    { "name": "dataset_location" , "type":  "string" },
    { "name": "repository", "type": "string" }
  ]
}
"""

status_record_schema = """
{
  "namespace": "com.filipovski.drboson.sharedkernel.integration.avro",
  "type": "record",
  "name": "RunStatusChangedRecord",
  "fields": [
    { "name": "run_id", "type":  "string" },
    { "name": "project_id", "type": "string" },
    { "name": "status" , 
      "type":  { 
        "name": "RunStatus", 
        "type": "enum", 
        "symbols": ["PENDING", "RUNNING", "COMPLETED", "FAILED"]
      }
    }
  ]
}
"""

log_record_schema = """
{
  "namespace": "com.filipovski.drboson.sharedkernel.integration.avro",
  "type": "record",
  "name": "MetricLogRecord",
  "fields": [
    { "name": "run_id", "type":  "string" },
    { "name": "project_id", "type": "string" },
    { "name": "log", "type": "string" }
  ]
}
"""

file_record_schema = """
{
  "namespace": "com.filipovski.drboson.sharedkernel.integration.avro",
  "type": "record",
  "name": "RunFileGeneratedRecord",
  "fields": [
    { "name": "run_id", "type":  "string" },
    { "name": "project_id", "type": "string" },
    { "name": "file_id", "type": "string" },
    { "name": "file_name", "type": "string" },
    { "name": "file_key", "type": "string" }
  ]
}
"""

dataset_record_schema = """
{
  "type": "record",
  "name": "DatasetRecord",
  "fields": [
    { "name": "dataset_id", "type":  "string" },
    { "name": "project_id", "type":  "string" },
    { "name": "name", "type": "string" }, 
    { "name": "description", "type":  "string" },
    { "name": "location", "type": "string" }, 
    { "name": "mimetype" , "type":  "string" }, 
    { "name": "dataset_type",
      "type": {
        "name": "DatasetType",
        "type": "enum",
        "symbols": ["COMMON", "RDF"]
      }
    }
  ]
}
"""

dataset_store_schema = """
{
  "namespace": "com.filipovski.drboson.sharedkernel.integration.avro",
  "type": "record",
  "name": "DatasetStorageJobRecord",
  "fields": [
    { "name": "job_id", "type":  "string" },
    { "name": "project_id", "type": "string" },
    { "name": "dataset_id", "type": "string" },
    { "name": "job_type",
      "type": {
        "name": "JobType",
        "type": "enum",
        "symbols": ["STORAGE", "RDF_READY_STORAGE", "RDF_TRANSFORM_STORAGE", "PROCESSING"]
      }
    },
    { "name": "dataset_location" , "type":  "string" },
    { "name": "dataset_name", "type": "string", "default": "" },
    { "name": "payload", "type":  "string", "default": "{}" }
  ]
}
"""

rdf_ready_storage_job_status_schema = """
{
  "type": "record",
  "name": "JobStatusRecord",
  "fields": [
    { "name": "job_id", "type":  "string" },
    { "name": "job_type",
      "type": {
        "name": "JobType",
        "type": "enum",
        "symbols": ["STORAGE", "RDF_READY_STORAGE", "RDF_TRANSFORM_STORAGE", "PROCESSING"]
      }
    },
    { "name": "dataset_id", "type":  "string" },
    { "name": "status" , 
      "type":  { 
        "name": "RunStatus", 
        "type": "enum", 
        "symbols": ["PENDING", "RUNNING", "COMPLETED", "FAILED"]
      } 
    },  
    { "name": "job_state", 
      "type": {
        "type": "record", 
        "name": "JobState",
        "fields": [
            { "name": "storage_location", "type": "string", "default": "" }
        ]
      }
    }, 
    { "name": "message", "type": "string" }
  ]
}
"""

storage_job_status_schema = """
{
  "namespace": "com.filipovski.drboson.sharedkernel.integration.avro",
  "type": "record",
  "name": "StorageJobStatusRecord",
  "fields": [
    { "name": "job_id", "type":  "string" },
    { "name": "job_type",
      "type": {
        "name": "JobType",
        "type": "enum",
        "symbols": ["STORAGE", "RDF_READY_STORAGE", "RDF_TRANSFORM_STORAGE", "PROCESSING"]
      }
    },
    { "name": "dataset_id", "type":  "string" },
    { "name": "status" ,
      "type":  {
        "name": "JobStatus",
        "type": "enum",
        "symbols": ["PENDING", "RUNNING", "COMPLETED", "FAILED"]
      }
    },
    { "name": "job_state",
      "type": {
        "type": "record",
        "name": "JobState",
        "fields": [
          { "name": "storage_location", "type": "string", "default": "" },
          { "name": "sample_name", "type": "string", "default": "" },
          {
            "name": "column_data",
            "type": {
              "type": "array",
              "items": {
                "name": "ColumnData",
                "type": "record",
                "fields": [
                  { "name": "name", "type": "string" },
                  { "name": "type", "type": "string" },
                  { "name": "noMissingValues", "type": "int" },
                  { "name": "noOK", "type": "int" }
                ]
              },
              "default": []
            }
          }
        ]
      }
    },
    { "name": "message", "type": "string" }
  ]
}
"""

