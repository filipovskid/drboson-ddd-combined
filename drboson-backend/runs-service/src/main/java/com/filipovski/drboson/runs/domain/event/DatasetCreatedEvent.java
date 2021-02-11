package com.filipovski.drboson.runs.domain.event;

import com.filipovski.drboson.runs.domain.model.DatasetId;
import com.filipovski.drboson.runs.domain.model.OwnerId;
import com.filipovski.drboson.runs.domain.model.ProjectId;
import com.filipovski.drboson.sharedkernel.domain.base.DomainEvent;
import com.filipovski.drboson.sharedkernel.integration.avro.DatasetCreatedRecord;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class DatasetCreatedEvent implements DomainEvent {

    private final DatasetId datasetId;

    private final OwnerId ownerId;

    private final ProjectId projectId;

    private final String location;

    private final String mimeType;

    private final Instant occuredOn;

    public DatasetCreatedEvent(DatasetId datasetId, OwnerId ownerId, ProjectId projectId, String location,
                                String mimeType, Instant occuredOn) {
        this.datasetId = Objects.requireNonNull(datasetId, "datasetId must not be null");
        this.ownerId = Objects.requireNonNull(ownerId, "datasetId must not be null");
        this.projectId = Objects.requireNonNull(projectId, "datasetId must not be null");
        this.occuredOn = Objects.requireNonNull(occuredOn, "occuredOn must not be null");

        if(Strings.isNullOrEmpty(location))
            throw new IllegalArgumentException("location must not be empty");

        if(Strings.isNullOrEmpty(mimeType))
            throw new IllegalArgumentException("mimeType must not be empty");

        this.location = location;
        this.mimeType = mimeType;
    }

    public static DatasetCreatedEvent from(DatasetCreatedRecord record) {
        return new DatasetCreatedEvent(
                DatasetId.from(record.getDatasetId()),
                OwnerId.from(record.getOwnerId()),
                ProjectId.from(record.getProjectId()),
                record.getLocation(),
                record.getMimeType(),
                Instant.ofEpochSecond(record.getTimestamp())
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}