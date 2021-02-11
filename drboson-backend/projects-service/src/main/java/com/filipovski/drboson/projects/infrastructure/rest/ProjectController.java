package com.filipovski.drboson.projects.infrastructure.rest;

import com.filipovski.drboson.projects.application.ProjectService;
import com.filipovski.drboson.projects.application.dtos.ProjectDto;
import com.filipovski.drboson.sharedkernel.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDto> getOwnerProjects(@AuthenticationPrincipal AuthenticatedUser owner) {
        return projectService.getOwnerProjects(owner.getUserId());
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@RequestBody ProjectDto projectDto,
                                    @AuthenticationPrincipal AuthenticatedUser owner) throws MalformedURLException {
        return projectService.createProject(owner.getUserId(), projectDto);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable String projectId,
                                                 @AuthenticationPrincipal AuthenticatedUser owner) {
        return ResponseEntity.ok(projectService.getOwnerProject(owner.getUserId(), projectId));
    }

    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@PathVariable String projectId, @AuthenticationPrincipal AuthenticatedUser owner)  {
        projectService.deleteProject(owner.getUserId(), projectId);
    }
}
