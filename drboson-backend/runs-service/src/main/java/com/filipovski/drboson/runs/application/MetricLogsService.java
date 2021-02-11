package com.filipovski.drboson.runs.application;

import com.filipovski.drboson.runs.application.dtos.ProjectMetricLogs;
import com.filipovski.drboson.runs.application.dtos.RunMetricLogs;

public interface MetricLogsService {

    RunMetricLogs getRunMetricLogs(String ownerId, String runId) throws Exception;

    ProjectMetricLogs getProjectMetricLogs(String ownerId, String projectId);
}
