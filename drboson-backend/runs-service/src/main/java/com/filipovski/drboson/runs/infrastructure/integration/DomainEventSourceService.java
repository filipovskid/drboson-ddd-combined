package com.filipovski.drboson.runs.infrastructure.integration;

import com.filipovski.drboson.runs.config.KafkaConfig;
import com.filipovski.drboson.runs.domain.event.RunStartedEvent;
import com.filipovski.drboson.sharedkernel.integration.avro.RunStartedRecord;
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
    public void onRunStartedEvent(@NonNull RunStartedEvent event) {
        RunStartedRecord record = RunStartedRecord.newBuilder()
                .setId(event.getId().getId())
                .setProjectId(event.getProjectId().getId())
                .setDatasetLocation(event.getLocation())
                .setRepository(event.getRepository())
                .build();

        kafkaTemplate.send(kafkaConfig.getRunsTopic(), event.getId().getId(), record);
    }

}
