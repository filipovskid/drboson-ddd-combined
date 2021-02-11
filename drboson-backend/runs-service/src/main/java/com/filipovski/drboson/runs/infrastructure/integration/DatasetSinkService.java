package com.filipovski.drboson.runs.infrastructure.integration;

import com.filipovski.drboson.runs.domain.event.DatasetCreatedEvent;
import com.filipovski.drboson.sharedkernel.integration.avro.DatasetCreatedRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@PropertySource("classpath:messaging/messaging.properties")
@KafkaListener(topics = "#{'${kafka.datasets.topic}'}", groupId = "run-datasets")
public class DatasetSinkService {

    private final ApplicationEventPublisher publisher;

    public DatasetSinkService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @KafkaHandler
    @Transactional
    public void onDatasetCreatedEvent(DatasetCreatedRecord record) {
        DatasetCreatedEvent event = DatasetCreatedEvent.from(record);

        publisher.publishEvent(event);
    }

}
