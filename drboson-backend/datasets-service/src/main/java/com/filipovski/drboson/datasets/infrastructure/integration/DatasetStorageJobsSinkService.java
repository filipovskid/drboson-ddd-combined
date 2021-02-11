package com.filipovski.drboson.datasets.infrastructure.integration;

import com.filipovski.drboson.datasets.domain.event.StorageJobStatusEvent;
import com.filipovski.drboson.datasets.domain.model.processing.ColumnSchema;
import com.filipovski.drboson.datasets.domain.model.processing.ColumnValidity;
import com.filipovski.drboson.datasets.domain.model.processing.ProcessingColumn;
import com.filipovski.drboson.sharedkernel.integration.avro.StorageJobStatusRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:messaging/messaging.properties")
@KafkaListener(topics = "#{'${kafka.dataset.storage.jobs.status.topic}'}", groupId = "storage-job-status")
public class DatasetStorageJobsSinkService {

    private final ApplicationEventPublisher publisher;

    public DatasetStorageJobsSinkService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @KafkaHandler
    @Transactional
    public void onStorageJobStatusEvent(StorageJobStatusRecord record) {
        List<ProcessingColumn> columns = record.getJobState().getColumnData().stream()
                .map(data -> ProcessingColumn.from(
                            ColumnSchema.from(data.getName(), data.getType()),
                            ColumnValidity.from(data.getNoMissingValues(), data.getNoOK())
                )).collect(Collectors.toList());

        StorageJobStatusEvent event = StorageJobStatusEvent.from(
                record.getJobId(),
                record.getStatus().name(),
                record.getJobType().name(),
                record.getDatasetId(),
                record.getJobState().getStorageLocation(),
                record.getJobState().getSampleName(),
                columns,
                Instant.now()
        );

        publisher.publishEvent(event);
    }

}
