package com.filipovski.drboson.runs.infrastructure.integration;

import com.filipovski.drboson.runs.domain.event.MetricLogCreatedEvent;
import com.filipovski.drboson.runs.domain.event.RunFileGeneratedEvent;
import com.filipovski.drboson.runs.domain.event.RunStatusChangedEvent;
import com.filipovski.drboson.sharedkernel.integration.avro.MetricLogRecord;
import com.filipovski.drboson.sharedkernel.integration.avro.RunFileGeneratedRecord;
import com.filipovski.drboson.sharedkernel.integration.avro.RunStatusChangedRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@PropertySource("classpath:messaging/messaging.properties")
public class RunWorkerSinkService {

    private final ApplicationEventPublisher publisher;

    public RunWorkerSinkService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Transactional
    @KafkaListener(topics = "#{'${kafka.run.logs.topic}'}", groupId = "run-logs")
    public void onMetricLogCreatedEvent(MetricLogRecord record) {
        MetricLogCreatedEvent event = MetricLogCreatedEvent.from(
                record.getRunId(),
                record.getProjectId(),
                record.getLog(),
                Instant.now()
        );

        publisher.publishEvent(event);
    }

    @Transactional
    @KafkaListener(topics = "#{'${kafka.run.files.topic}'}", groupId = "run-files")
    public void onRunFileGeneratedEvent(RunFileGeneratedRecord record) {
        RunFileGeneratedEvent event = RunFileGeneratedEvent.from(
                record.getRunId(),
                record.getProjectId(),
                record.getFileName(),
                record.getFileKey(),
                "drboson/unknown",
                Instant.now()
        );

        publisher.publishEvent(event);
    }

    @Transactional
    @KafkaListener(topics = "#{'${kafka.run.status.topic}'}", groupId = "run-files")
    public void onRunStatusChangedEvent(RunStatusChangedRecord record) {
        RunStatusChangedEvent event = RunStatusChangedEvent.from(
                record.getRunId(),
                record.getProjectId(),
                record.getStatus().name()
        );

        publisher.publishEvent(event);
    }

}
