package com.filipovski.drboson.runs.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.filipovski.drboson.runs.domain.model.Run;
import com.filipovski.drboson.runs.domain.model.metrics.LogData;
import com.filipovski.drboson.runs.domain.model.metrics.MetricLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class RunMetricLogs {

    @JsonProperty("run_id")
    private String runId;

    @JsonProperty("run_name")
    private String runName;

    @JsonProperty("logs")
    @JsonRawValue
    private List<String> logs;

    public static RunMetricLogs from(Run run) {
        List<String> logs = run.getLogs().stream()
                .map(MetricLog::getLog)
                .map(LogData::log)
                .collect(Collectors.toList());

        return new RunMetricLogs(run.id().getId(), run.getName().name(), logs);
    }

}
