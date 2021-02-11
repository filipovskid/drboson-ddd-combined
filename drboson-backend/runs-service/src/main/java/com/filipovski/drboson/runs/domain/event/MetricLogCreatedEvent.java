package com.filipovski.drboson.runs.domain.event;

import com.filipovski.drboson.runs.domain.model.ProjectId;
import com.filipovski.drboson.runs.domain.model.RunId;
import com.filipovski.drboson.runs.domain.model.metrics.LogData;
import com.filipovski.drboson.sharedkernel.domain.base.DomainEvent;
import lombok.Getter;

import java.time.Instant;

@Getter
public class MetricLogCreatedEvent implements DomainEvent {

    private final RunId runId;

    private final ProjectId projectId;

    private final LogData log;

    private final Instant occuredOn;

    private MetricLogCreatedEvent(RunId runId, ProjectId projectId, LogData log, Instant occuredOn) {
        this.runId = runId;
        this.projectId = projectId;
        this.log = log;
        this.occuredOn = occuredOn;
    }

    public static MetricLogCreatedEvent from(String runId, String projectId, String log, Instant occuredOn) {
        return new MetricLogCreatedEvent(
                RunId.from(runId),
                ProjectId.from(projectId),
                LogData.from(log),
                occuredOn
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
