package com.filipovski.drboson.projects.application.impl;

import com.filipovski.drboson.projects.application.ProjectService;
import com.filipovski.drboson.projects.application.dtos.ProjectDto;
import com.filipovski.drboson.projects.domain.model.OwnerId;
import com.filipovski.drboson.projects.domain.model.Project;
import com.filipovski.drboson.projects.domain.model.ProjectId;
import com.filipovski.drboson.projects.domain.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectDto createProject(String ownerId, ProjectDto projectData) throws MalformedURLException {
        Project project = Project.from(ownerId, projectData.getName(),
                projectData.getDescription(),
                projectData.getRepository());

        return ProjectDto.from(projectRepository.save(project));
    }

    @Override
    public List<ProjectDto> getOwnerProjects(String ownerId) {
        List<Project> projects = projectRepository.findOwnerProjects(OwnerId.from(ownerId));

        return projects.stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto getOwnerProject(String ownerId, String projectId) {
        Project project = projectRepository.findOwnerProject(OwnerId.from(ownerId), ProjectId.from(projectId))
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return ProjectDto.from(project);
    }

    @Override
    public void deleteProject(String ownerId, String projectId) {
        projectRepository.deleteOwnerProject(OwnerId.from(ownerId), ProjectId.from(projectId));
    }
}
