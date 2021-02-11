package com.filipovski.drboson.runs.application.impl;

import com.filipovski.drboson.runs.application.RunService;
import com.filipovski.drboson.runs.application.data.DatasetIntegrationRepository;
import com.filipovski.drboson.runs.application.dtos.RunDto;
import com.filipovski.drboson.runs.domain.model.*;
import com.filipovski.drboson.runs.domain.repository.RunRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RunServiceImpl implements RunService {

    private final RunRepository runRepository;
    private final DatasetIntegrationRepository datasetRepository;

    public RunServiceImpl(RunRepository runRepository,
                          DatasetIntegrationRepository datasetRepository) {
        this.runRepository = runRepository;
        this.datasetRepository = datasetRepository;
    }

    @Override
    public RunDto createRun(String ownerId, RunDto runData) throws Exception {
        Dataset dataset = datasetRepository.findOwnerDataset(OwnerId.from(ownerId), DatasetId.from(runData.getDatasetId()))
                .orElseThrow(() -> new Exception("Dataset not found"));

        Run run = Run.builder()
                .ownerId(ownerId)
                .projectId(runData.getProjectId())
                .name(runData.getName())
                .description(runData.getDescription())
                .repository(runData.getRepository())
                .dataset(dataset)
                .build();

        run = runRepository.save(run);

        return startRun(ownerId, run.id().getId());
    }

    @Override
    public RunDto getRun(String ownerId, String runId) throws Exception {
        Run run = runRepository.findOwnerRun(OwnerId.from(ownerId), RunId.from(runId))
                .orElseThrow(() -> new Exception("Run not found"));

        return RunDto.from(run);
    }

    @Override
    public List<RunDto> getProjectRuns(String ownerId, String projectId) {
        return runRepository.findProjectRuns(OwnerId.from(ownerId), ProjectId.from(projectId)).stream()
                .map(RunDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRun(String ownerId, String runId) throws Exception {
        Run run = runRepository.findOwnerRun(OwnerId.from(ownerId), RunId.from(runId))
                .orElseThrow(() -> new Exception("Run not found"));

        runRepository.deleteOwnerRun(OwnerId.from(ownerId), run.id());
    }

    @Override
    public RunDto startRun(String ownerId, String runId) throws Exception {
        Run run = runRepository.findOwnerRun(OwnerId.from(ownerId), RunId.from(runId))
                .orElseThrow(() -> new Exception("Run not found"));

        run.start();

        // Emit event for starting the run
        run = runRepository.save(run);

        return RunDto.from(run);
    }
}
