package com.filipovski.drboson.datasets.application.impl;

import com.filipovski.drboson.datasets.application.DatasetProcessingService;
import com.filipovski.drboson.datasets.application.dtos.processing.RefreshColumnsResponse;
import com.filipovski.drboson.datasets.domain.model.Dataset;
import com.filipovski.drboson.datasets.domain.model.DatasetId;
import com.filipovski.drboson.datasets.domain.model.OwnerId;
import com.filipovski.drboson.datasets.domain.repository.DatasetRepository;
import org.springframework.stereotype.Service;

@Service
public class DatasetProcessingServiceImpl implements DatasetProcessingService {

    private final DatasetRepository datasetRepository;

    public DatasetProcessingServiceImpl(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    @Override
    public RefreshColumnsResponse getProcessingState(String ownerId, String datasetId) throws Exception {
        Dataset dataset = datasetRepository.findOwnerDataset(OwnerId.from(ownerId), DatasetId.from(datasetId))
                .orElseThrow(() -> new Exception("Dataset not found"));

        return RefreshColumnsResponse.from(dataset.getProcessing());
    }
}
