package com.filipovski.drboson.runs.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectMetricLogs {

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("logs")
    private List<RunMetricLogs> runMetricLogs;

    public static ProjectMetricLogs from(String projectId, List<RunMetricLogs> runMetricLogs) {
        return new ProjectMetricLogs(projectId, runMetricLogs);
    }
}
