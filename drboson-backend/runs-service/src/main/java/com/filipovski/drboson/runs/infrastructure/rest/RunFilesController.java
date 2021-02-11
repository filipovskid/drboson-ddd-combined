package com.filipovski.drboson.runs.infrastructure.rest;

import com.filipovski.drboson.runs.application.dtos.DownloadRunFileResponse;
import com.filipovski.drboson.runs.application.dtos.RunFileDto;
import com.filipovski.drboson.runs.application.impl.RunFilesService;
import com.filipovski.drboson.sharedkernel.security.AuthenticatedUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/run", produces = MediaType.APPLICATION_JSON_VALUE)
public class RunFilesController {

    private final RunFilesService runFilesService;

    public RunFilesController(RunFilesService runFilesService) {
        this.runFilesService = runFilesService;
    }

    @GetMapping("/{run-id}/files")
    public ResponseEntity<List<RunFileDto>> getRunFiles(@AuthenticationPrincipal AuthenticatedUser user,
                                                        @PathVariable("run-id") String runId) throws Exception {

        return ResponseEntity.ok(runFilesService.getRunFiles(user.getUserId(), runId));
    }

    @GetMapping("/{run-id}/download-file")
    public ResponseEntity<StreamingResponseBody> downloadRunFile(@AuthenticationPrincipal AuthenticatedUser user,
                                                                 @PathVariable("run-id") String runId,
                                                                 @RequestParam("file_id") String fileId,
                                                                 HttpServletResponse response) throws Exception {

        DownloadRunFileResponse downloadResponse = runFilesService.downloadRunFile(user.getUserId(), runId, fileId);
        response.setHeader("Content-Disposition", "attachment; filename=" + downloadResponse.getName());

        return ResponseEntity.ok(downloadResponse.getResponseBody());
    }
}
