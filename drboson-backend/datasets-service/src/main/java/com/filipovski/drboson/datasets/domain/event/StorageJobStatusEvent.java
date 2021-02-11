package com.filipovski.drboson.datasets.domain.event;

import com.filipovski.drboson.datasets.domain.model.*;
import com.filipovski.drboson.datasets.domain.model.processing.ColumnSchema;
import com.filipovski.drboson.datasets.domain.model.processing.ColumnValidity;
import com.filipovski.drboson.datasets.domain.model.processing.DatasetSample;
import com.filipovski.drboson.datasets.domain.model.processing.ProcessingColumn;
import com.filipovski.drboson.sharedkernel.domain.base.DomainEvent;
import lombok.Getter;
import org.opengis.metadata.lineage.Processing;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
public class StorageJobStatusEvent implements DomainEvent {

    private final JobId jobId;

    private final JobStatus jobStatus;

    private final JobType jobType;

    private final DatasetId datasetId;

    private final DataSource source;

    private final DatasetSample sample;

    private final List<ProcessingColumn> columns;

    private final Instant occuredOn;

    public StorageJobStatusEvent(JobId jobId, JobStatus jobStatus, JobType jobType, DatasetId datasetId,
                                 DataSource source, DatasetSample sample, List<ProcessingColumn> columns,
                                 Instant occuredOn) {

        this.jobId = jobId;
        this.jobStatus = jobStatus;
        this.jobType = jobType;
        this.datasetId = datasetId;
        this.source = source;
        this.sample = sample;
        this.columns = columns;
        this.occuredOn = occuredOn;
    }

    public static StorageJobStatusEvent from(String jobId, String jobStatus, String jobType, String datasetId,
                                             String location, String sampleName,  List<ProcessingColumn> columns,
                                             Instant occuredOn) {

        Objects.requireNonNull(columns, "columns must not be null");
        Objects.requireNonNull(occuredOn, "occuredOn must not be null");

        return new StorageJobStatusEvent(
                JobId.from(jobId),
                JobStatus.valueOf(jobStatus),
                JobType.valueOf(jobType),
                DatasetId.from(datasetId),
                DataSource.from(location, "drboson/data"),
                DatasetSample.from(sampleName),
                columns,
                occuredOn
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
