package com.filipovski.drboson.runs.infrastructure.rest;

import com.filipovski.drboson.runs.application.RunService;
import com.filipovski.drboson.runs.application.dtos.RunDto;
import com.filipovski.drboson.runs.domain.model.RunId;
import com.filipovski.drboson.sharedkernel.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/run", produces = MediaType.APPLICATION_JSON_VALUE)
public class RunController {

    private final RunService runService;

    public RunController(RunService runService) {
        this.runService = runService;
    }

    @PostMapping("/create")
    public ResponseEntity<RunDto> createRun(@AuthenticationPrincipal AuthenticatedUser user,
                                            @RequestBody RunDto runData) throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(runService.createRun(user.getUserId(), runData));
    }

    @GetMapping("/{run-id}")
    public ResponseEntity<RunDto> getRun(@AuthenticationPrincipal AuthenticatedUser user,
                                         @PathVariable("run-id") String runId) throws Exception {

        return ResponseEntity.ok(runService.getRun(user.getUserId(), runId));
    }

    @GetMapping("/project-runs")
    public ResponseEntity<List<RunDto>> getProjectRuns(@AuthenticationPrincipal AuthenticatedUser user,
                                                       @RequestParam("project_id") String projectId) {

        return ResponseEntity.ok(runService.getProjectRuns(user.getUserId(), projectId));
    }

    @DeleteMapping("/{run-id}")
    public void deleteRun(@AuthenticationPrincipal AuthenticatedUser user,
                          @PathVariable("run-id") String runId) throws Exception {

        runService.deleteRun(user.getUserId(), runId);
    }

}
