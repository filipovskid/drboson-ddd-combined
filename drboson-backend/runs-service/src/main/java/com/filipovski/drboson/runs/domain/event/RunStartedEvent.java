package com.filipovski.drboson.runs.domain.event;

import com.filipovski.drboson.runs.domain.model.ProjectId;
import com.filipovski.drboson.runs.domain.model.Run;
import com.filipovski.drboson.runs.domain.model.RunId;
import com.filipovski.drboson.sharedkernel.domain.base.DomainEvent;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class RunStartedEvent implements DomainEvent {

    private final RunId id;

    private final ProjectId projectId;

    private final String location;

    private final String repository;

    private final Instant occuredOn;

    public RunStartedEvent(RunId runId, ProjectId projectId, String location, String repository) {
        this.id = Objects.requireNonNull(runId, "runId must not be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId must not be null");

        if (Strings.isNullOrEmpty(location))
            throw new IllegalArgumentException("location must not be empty");

        if (Strings.isNullOrEmpty(repository))
            throw new IllegalArgumentException("repository must not be empty");

        this.location = location;
        this.repository = repository;
        this.occuredOn = Instant.now();
    }

    public static RunStartedEvent from(Run run) {
        return new RunStartedEvent(
                run.id(),
                run.getProjectId(),
                run.getDataset().getDataSource().location(),
                run.getRepository().repository()
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
