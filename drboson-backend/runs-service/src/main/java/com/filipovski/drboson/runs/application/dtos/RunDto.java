package com.filipovski.drboson.runs.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.filipovski.drboson.runs.domain.model.Run;
import lombok.Getter;

@Getter
public class RunDto {

    private String id;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("dataset_id")
    private String datasetId;

    private String name;

    private String description;

    private String repository;

    private String status;

    public RunDto(String id, String projectId, String datasetId, String name, String description,
                  String repository, String status) {
        this.id = id;
        this.projectId = projectId;
        this.datasetId = datasetId;
        this.name = name;
        this.description = description;
        this.repository = repository;
        this.status = status;
    }

    public static RunDto from(Run run) {
        return new RunDto(
                run.id().getId(),
                run.getProjectId().getId(),
                run.getDataset().id().getId(),
                run.getName().name(),
                run.getDescription().description(),
                run.getRepository().repository(),
                run.getStatus().name()
        );
    }
}
