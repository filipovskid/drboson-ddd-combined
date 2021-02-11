package com.filipovski.drboson.runs.application.impl;

import com.filipovski.drboson.runs.application.MetricLogsService;
import com.filipovski.drboson.runs.application.dtos.ProjectMetricLogs;
import com.filipovski.drboson.runs.application.dtos.RunMetricLogs;
import com.filipovski.drboson.runs.domain.model.OwnerId;
import com.filipovski.drboson.runs.domain.model.ProjectId;
import com.filipovski.drboson.runs.domain.model.Run;
import com.filipovski.drboson.runs.domain.model.RunId;
import com.filipovski.drboson.runs.domain.repository.RunRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetricLogsServiceImpl implements MetricLogsService {

    private final RunRepository runRepository;

    public MetricLogsServiceImpl(RunRepository runRepository) {
        this.runRepository = runRepository;
    }

    @Override
    public RunMetricLogs getRunMetricLogs(String ownerId, String runId) throws Exception {
        Run run = runRepository.findOwnerRun(OwnerId.from(ownerId), RunId.from(runId))
                .orElseThrow(() -> new Exception("Run not found"));

        return RunMetricLogs.from(run);
    }

    @Override
    public ProjectMetricLogs getProjectMetricLogs(String ownerId, String projectId) {
        List<RunMetricLogs> runMetricLogs = runRepository
                .findProjectRuns(OwnerId.from(ownerId), ProjectId.from(projectId))
                .stream()
                .map(RunMetricLogs::from)
                .collect(Collectors.toList());

        return ProjectMetricLogs.from(projectId, runMetricLogs);
    }
}
