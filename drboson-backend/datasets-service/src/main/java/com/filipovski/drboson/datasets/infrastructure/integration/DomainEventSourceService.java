package com.filipovski.drboson.datasets.infrastructure.integration;

import com.filipovski.drboson.datasets.config.KafkaConfig;
import com.filipovski.drboson.datasets.domain.event.DatasetCreatedEvent;
import com.filipovski.drboson.datasets.domain.event.DatasetStorageJobEvent;
import com.filipovski.drboson.sharedkernel.integration.avro.DataType;
import com.filipovski.drboson.sharedkernel.integration.avro.DatasetCreatedRecord;
import com.filipovski.drboson.sharedkernel.integration.avro.DatasetStorageJobRecord;
import com.filipovski.drboson.sharedkernel.integration.avro.JobType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class DomainEventSourceService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConfig kafkaConfig;

    public DomainEventSourceService(KafkaTemplate<String, Object> kafkaTemplate,
                                    KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfig = kafkaConfig;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onDatasetCreatedEvent(@NonNull DatasetCreatedEvent event) {
        DatasetCreatedRecord record = DatasetCreatedRecord.newBuilder()
                .setDatasetId(event.getDatasetId().getId())
                .setOwnerId(event.getOwnerId().getId())
                .setProjectId(event.getProjectId().getId())
                .setLocation(event.getLocation())
                .setMimeType(event.getMimeType())
                .setDataType(DataType.valueOf(event.getDataType().name()))
                .setTimestamp(event.getOccuredOn().getEpochSecond())
                .build();

        kafkaTemplate.send(kafkaConfig.getDatasetsTopic(), event.getDatasetId().getId(), record);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onDatasetStorageJobEvent(@NonNull DatasetStorageJobEvent event) {
        DatasetStorageJobRecord record = DatasetStorageJobRecord.newBuilder()
                .setJobId(event.getJobId().getId())
                .setProjectId(event.getProjectId().getId())
                .setDatasetId(event.getDatasetId().getId())
                .setJobType(JobType.valueOf(event.getJobType().name()))
                .setDatasetLocation(event.getLocation())
                .setDatasetName(event.getDatasetName())
                .build();

        kafkaTemplate.send(kafkaConfig.getDatasetStorageJobsTopic(), event.getDatasetId().getId(), record);
    }
}
