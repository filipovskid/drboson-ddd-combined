package com.filipovski.drboson.runs.domain.event;

import com.filipovski.drboson.runs.domain.model.DataSource;
import com.filipovski.drboson.runs.domain.model.ProjectId;
import com.filipovski.drboson.runs.domain.model.RunId;
import com.filipovski.drboson.runs.domain.model.files.FileName;
import com.filipovski.drboson.sharedkernel.domain.base.DomainEvent;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;

@Getter
public class RunFileGeneratedEvent implements DomainEvent {

    private final RunId runId;

    private final ProjectId projectId;

    private final FileName fileName;

    private final DataSource dataSource;

    private final Instant occuredOn;

    private RunFileGeneratedEvent(RunId runId, ProjectId projectId, FileName fileName, DataSource dataSource,
                                  Instant occuredOn) {
        this.runId = runId;
        this.projectId = projectId;
        this.fileName = fileName;
        this.dataSource = dataSource;
        this.occuredOn = occuredOn;

    }

    public static RunFileGeneratedEvent from(String runId, String projectId, String fileName, String location,
                                             String mimeType, Instant occuredOn) {
        return new RunFileGeneratedEvent(
                RunId.from(runId),
                ProjectId.from(projectId),
                FileName.from(fileName),
                DataSource.from(location, mimeType),
                occuredOn
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
