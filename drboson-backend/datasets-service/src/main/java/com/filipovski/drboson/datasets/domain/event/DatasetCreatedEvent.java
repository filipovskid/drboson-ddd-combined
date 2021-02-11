package com.filipovski.drboson.datasets.domain.event;

import com.filipovski.drboson.datasets.domain.model.*;
import com.filipovski.drboson.sharedkernel.domain.base.DomainEvent;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class DatasetCreatedEvent implements DomainEvent {

    private final DatasetId datasetId;

    private final OwnerId ownerId;

    private final OwnerProjectId projectId;

    private final String location;

    private final String mimeType;

    private final DataType dataType;

    private final Instant occuredOn;

    public DatasetCreatedEvent(DatasetId datasetId, OwnerId ownerId, OwnerProjectId projectId, String location,
                               String mimeType, DataType dataType) {
        this.datasetId = Objects.requireNonNull(datasetId, "datasetId must not be null");
        this.ownerId = Objects.requireNonNull(ownerId, "ownerId must not be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId must not be null");

        this.location = location;
        this.mimeType = mimeType;
        this.dataType = dataType;
        this.occuredOn = Instant.now();

    }

    public static DatasetCreatedEvent from(Dataset dataset) {
        return new DatasetCreatedEvent(
                dataset.id(),
                dataset.getOwnerId(),
                dataset.getOwnerProjectId(),
                dataset.getDataSource().location(),
                dataset.getDataSource().mimeType(),
                dataset.getDataSource().type()
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
