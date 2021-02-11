package com.filipovski.drboson.users.integration;

import com.filipovski.drboson.sharedkernel.integration.avro.UserCreatedRecord;
import com.filipovski.drboson.users.config.KafkaConfig;
import com.filipovski.drboson.users.domain.event.UserCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class DomainEventSourceService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConfig config;

    public DomainEventSourceService(ApplicationEventPublisher applicationEventPublisher,
                                    KafkaTemplate<String, Object> kafkaTemplate,
                                    KafkaConfig config) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.kafkaTemplate = kafkaTemplate;
        this.config = config;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onDomainEvent(@NonNull UserCreatedEvent event) {
        UserCreatedRecord record = UserCreatedRecord.newBuilder()
                .setUserId(event.getUserId().getId())
                .setUsername(event.getUsername())
                .setPassword(event.getPassword())
                .setEmail(event.getEmail())
                .setTimestamp(event.occuredOn().getEpochSecond())
                .build();

        kafkaTemplate.send(config.getUsersTopic(), event.getUserId().getId(), record);
    }
}
