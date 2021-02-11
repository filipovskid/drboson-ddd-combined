package com.filipovski.drboson.runs.domain.event;

import com.filipovski.drboson.runs.domain.model.ProjectId;
import com.filipovski.drboson.runs.domain.model.RunId;
import com.filipovski.drboson.runs.domain.model.RunStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RunStatusChangedEvent {

    private final RunId runId;

    private final ProjectId projectId;

    private final RunStatus status;

    public static RunStatusChangedEvent from(String runId, String projectId, String status) {
        return new RunStatusChangedEvent(
                RunId.from(runId),
                ProjectId.from(projectId),
                RunStatus.valueOf(status)
        );
    }
}
