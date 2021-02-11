package com.filipovski.drboson.datasets.application;

import com.filipovski.drboson.datasets.application.dtos.processing.RefreshColumnsResponse;

public interface DatasetProcessingService {
    RefreshColumnsResponse getProcessingState(String ownerId, String datasetId) throws Exception;
}
