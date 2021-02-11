package com.filipovski.drboson.datasets.application.impl;

import com.filipovski.drboson.datasets.domain.event.StorageJobStatusEvent;
import com.filipovski.drboson.datasets.domain.model.*;
import com.filipovski.drboson.datasets.domain.model.processing.DataProcessing;
import com.filipovski.drboson.datasets.domain.repository.DatasetRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class DatasetWorkerIntegrationService {

    private final DatasetRepository datasetRepository;

    public DatasetWorkerIntegrationService(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onStorageJobStatusEvent(@NonNull StorageJobStatusEvent event) throws Exception {
        Dataset dataset = datasetRepository.findById(event.getDatasetId())
                .orElseThrow(() -> new Exception("Dataset not found exception"));

        DatasetJob job = dataset.getJobs().stream()
                .filter(j -> j.id().getId().equals(event.getJobId().getId()))
                .findFirst().orElseThrow(() -> new Exception("DatasetJob not found exception"));

        if(event.getJobStatus() == JobStatus.COMPLETED) {
            dataset.addLocalStorage(DataStorage.createStorage(DataStorageType.LOCAL, event.getSource()));

            DataProcessing processing = DataProcessing.builder()
                    .hasResult(true)
                    .build();

            processing.initializeProcessing(event.getColumns(), event.getSample(), processing.getResultState());
            dataset.createProcessing(processing);
        }

        job.updateStatus(event.getJobStatus().name());
        datasetRepository.save(dataset);
    }
}
