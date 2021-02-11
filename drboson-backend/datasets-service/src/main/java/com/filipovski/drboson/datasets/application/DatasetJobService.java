package com.filipovski.drboson.datasets.application;

import com.filipovski.drboson.datasets.application.dtos.DatasetDto;

public interface DatasetJobService {
    DatasetDto storeDatasetLocally(String ownerId, String datasetId) throws Exception;
}
