package com.filipovski.drboson.datasets.application.impl;

import com.filipovski.drboson.datasets.application.DatasetJobService;
import com.filipovski.drboson.datasets.application.dtos.DatasetDto;
import com.filipovski.drboson.datasets.domain.model.Dataset;
import com.filipovski.drboson.datasets.domain.model.DatasetId;
import com.filipovski.drboson.datasets.domain.model.OwnerId;
import com.filipovski.drboson.datasets.domain.repository.DatasetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DatasetJobServiceImpl implements DatasetJobService {

    private final DatasetRepository datasetRepository;

    public DatasetJobServiceImpl(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    @Override
    public DatasetDto storeDatasetLocally(String ownerId, String datasetId) throws Exception {
        Dataset dataset = datasetRepository.findOwnerDataset(OwnerId.from(ownerId), DatasetId.from(datasetId))
                .orElseThrow(() -> new Exception("Dataset not found"));

        dataset.storeDatasetLocally();

        // Dispatch DatasetStorageJobEvent
        return DatasetDto.from(datasetRepository.save(dataset));
    }
}
