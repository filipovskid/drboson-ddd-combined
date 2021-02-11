package com.filipovski.drboson.datasets.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class CreateDatasetRequest {
    private String projectId;
    private String name;
    private String description;
    private MultipartFile file;

    public static CreateDatasetRequest from(DatasetDto dto, MultipartFile file) {
        return new CreateDatasetRequest(dto.getProjectId(),
                dto.getName(),
                dto.getDescription(),
                file);
    }
}
