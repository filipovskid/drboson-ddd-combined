package com.filipovski.drboson.projects.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.filipovski.drboson.projects.domain.model.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectDto {
    private String id;
    private String name;
    private String description;
    private String repository;

    public static ProjectDto from(Project project) {
        return new ProjectDto(
                project.id().getId(),
                project.getName().name(),
                project.getDescription().description(),
                project.getRepository().toString()
        );
    }
}
