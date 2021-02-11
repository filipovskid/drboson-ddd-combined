package com.filipovski.drboson.runs.infrastructure.rest;

import com.filipovski.drboson.runs.application.MetricLogsService;
import com.filipovski.drboson.runs.application.dtos.ProjectMetricLogs;
import com.filipovski.drboson.runs.application.dtos.RunMetricLogs;
import com.filipovski.drboson.sharedkernel.security.AuthenticatedUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/run", produces = MediaType.APPLICATION_JSON_VALUE)
public class MetricLogsController {

    private final MetricLogsService metricLogsService;

    public MetricLogsController(MetricLogsService metricLogsService) {
        this.metricLogsService = metricLogsService;
    }

    @GetMapping("{run-id}/logs")
    public ResponseEntity<RunMetricLogs> getRunMetrics(@AuthenticationPrincipal AuthenticatedUser user,
                                                       @PathVariable("run-id") String runId) throws Exception {

        return ResponseEntity.ok(metricLogsService.getRunMetricLogs(user.getUserId(), runId));
    }

    @GetMapping("/project-logs")
    public ResponseEntity<ProjectMetricLogs> getProjectRunMetrics(@AuthenticationPrincipal AuthenticatedUser user,
                                                                  @RequestParam("project_id") String projectId) {

        return ResponseEntity.ok(metricLogsService.getProjectMetricLogs(user.getUserId(), projectId));
    }
}
