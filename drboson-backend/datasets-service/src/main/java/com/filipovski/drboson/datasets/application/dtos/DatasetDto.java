package com.filipovski.drboson.datasets.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.filipovski.drboson.datasets.domain.model.Dataset;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DatasetDto {

    private String id;

    @JsonProperty("project_id")
    private String projectId;

    private String name;

    private String description;

    @JsonProperty("storage_status")
    private String storageStatus;

    public static DatasetDto from(Dataset dataset) {
        return new DatasetDto(dataset.id().getId(),
                dataset.getOwnerProjectId().getId(),
                dataset.getName().name(),
                dataset.getDescription().description(),
                dataset.localStorageStatus().name()
        );
    }
}
