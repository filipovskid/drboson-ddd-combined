package com.filipovski.drboson.projects.application;

import com.filipovski.drboson.projects.application.dtos.ProjectDto;

import java.net.MalformedURLException;
import java.util.List;

public interface ProjectService {
    ProjectDto createProject(String ownerId, ProjectDto project) throws MalformedURLException;

    List<ProjectDto> getOwnerProjects(String ownerId);

    ProjectDto getOwnerProject(String ownerId, String projectId);

    void deleteProject(String ownerId, String projectId);
}
