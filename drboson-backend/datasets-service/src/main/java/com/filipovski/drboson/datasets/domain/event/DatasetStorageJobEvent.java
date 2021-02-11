package com.filipovski.drboson.datasets.domain.event;

import com.filipovski.drboson.datasets.domain.model.*;
import com.filipovski.drboson.sharedkernel.domain.base.DomainEvent;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class DatasetStorageJobEvent implements DomainEvent {

    private final JobId jobId;

    private final JobType jobType;

    private final DatasetId datasetId;

    private final OwnerProjectId projectId;

    private final String location;

    private final String datasetName;

    private final Instant occuredOn;

    public DatasetStorageJobEvent(JobId jobId, JobType jobType, DatasetId datasetId, OwnerProjectId projectId,
                                  String location, String datasetName) {
        this.jobId = Objects.requireNonNull(jobId, "jobId must not be null");
        this.jobType = Objects.requireNonNull(jobType, "jobType must not be null");
        this.datasetId = Objects.requireNonNull(datasetId, "datasetId must not be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId must not be null");

        this.location = location;
        this.datasetName = datasetName;
        this.occuredOn = Instant.now();
    }

    public static DatasetStorageJobEvent from (Dataset dataset, DatasetJob job) {
        return new DatasetStorageJobEvent(
                job.id(),
                job.getType(),
                dataset.id(),
                dataset.getOwnerProjectId(),
                dataset.getDataSource().location(),
                dataset.getName().name()
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
