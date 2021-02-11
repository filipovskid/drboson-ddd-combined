package com.filipovski.drboson.datasets.application;

import com.filipovski.drboson.datasets.application.dtos.CreateDatasetRequest;
import com.filipovski.drboson.datasets.application.dtos.DatasetDto;
import com.filipovski.drboson.datasets.application.dtos.DownloadDatasetResponse;

import java.util.List;

public interface DatasetService {
    DatasetDto createDataset(String ownerId, CreateDatasetRequest request) throws Exception;

    List<DatasetDto> getProjectDatasets(String ownerId, String projectId);

    DatasetDto getDataset(String ownerId, String datasetId) throws Exception;

    DownloadDatasetResponse downloadDatasetContent(String ownerId, String datasetId) throws Exception;

    void deleteDataset(String ownerId, String datasetId) throws Exception;
}
