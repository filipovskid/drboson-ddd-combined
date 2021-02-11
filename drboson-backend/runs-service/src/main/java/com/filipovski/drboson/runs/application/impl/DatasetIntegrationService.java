package com.filipovski.drboson.runs.application.impl;

import com.filipovski.drboson.runs.application.data.DatasetIntegrationRepository;
import com.filipovski.drboson.runs.domain.event.DatasetCreatedEvent;
import com.filipovski.drboson.runs.domain.model.Dataset;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class DatasetIntegrationService {

    private final DatasetIntegrationRepository datasetRepository;

    public DatasetIntegrationService(DatasetIntegrationRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onDatasetCreatedEvent(@NonNull DatasetCreatedEvent event) {
        Dataset dataset = Dataset.builder()
                .datasetId(event.getDatasetId().getId())
                .ownerId(event.getOwnerId().getId())
                .projectId(event.getProjectId().getId())
                .location(event.getLocation())
                .mimeType(event.getMimeType())
                .build();

        datasetRepository.save(dataset);
    }
}
