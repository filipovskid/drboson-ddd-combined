package com.filipovski.drboson.runs.application;

import com.filipovski.drboson.runs.application.dtos.RunDto;
import com.filipovski.drboson.runs.domain.model.Run;

import java.util.List;

public interface RunService {

    RunDto createRun(String ownerId, RunDto run) throws Exception;

    RunDto getRun(String ownerId, String runId) throws Exception;

    List<RunDto> getProjectRuns(String ownerId, String projectId);

    void deleteRun(String ownerId, String runId) throws Exception;

    RunDto startRun(String ownerId, String runId) throws Exception;
}
