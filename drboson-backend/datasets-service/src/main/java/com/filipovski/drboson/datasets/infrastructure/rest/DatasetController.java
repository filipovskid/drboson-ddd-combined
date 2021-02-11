package com.filipovski.drboson.datasets.infrastructure.rest;

import com.filipovski.drboson.datasets.application.DatasetService;
import com.filipovski.drboson.datasets.application.dtos.CreateDatasetRequest;
import com.filipovski.drboson.datasets.application.dtos.DatasetDto;
import com.filipovski.drboson.datasets.application.dtos.DownloadDatasetResponse;
import com.filipovski.drboson.sharedkernel.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping(value = "/dataset", produces = MediaType.APPLICATION_JSON_VALUE)
public class DatasetController {

    private final DatasetService datasetService;

    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DatasetDto> createDataset(@AuthenticationPrincipal AuthenticatedUser owner,
                                                    DatasetDto dataset,
                                                    @RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(datasetService.createDataset(owner.getUserId(), CreateDatasetRequest.from(dataset, file)));
    }

    @GetMapping("/project-datasets")
    public ResponseEntity<List<DatasetDto>> getProjectDatasets(@AuthenticationPrincipal AuthenticatedUser owner,
                                                               @RequestParam("project_id") String projectId) {

        return ResponseEntity.ok(datasetService.getProjectDatasets(owner.getUserId(), projectId));
    }

    @GetMapping("/{dataset-id}")
    public ResponseEntity<DatasetDto> getDataset(@AuthenticationPrincipal AuthenticatedUser user,
                                                 @PathVariable("dataset-id") String datasetId) throws Exception {

        return ResponseEntity.ok(datasetService.getDataset(user.getUserId(), datasetId));
    }

    @GetMapping("/{dataset-id}/download")
    public ResponseEntity<StreamingResponseBody> downloadDataset(@AuthenticationPrincipal AuthenticatedUser owner,
                                                                 @PathVariable("dataset-id") String datasetId,
                                                                 HttpServletResponse response) throws Exception {
        DownloadDatasetResponse downloadResponse = datasetService.downloadDatasetContent(owner.getUserId(), datasetId);
        response.setContentType(downloadResponse.getMIMEType());
        response.setHeader("Content-Disposition", "attachment; filename=" + downloadResponse.getName());

        return ResponseEntity.ok(downloadResponse.getResponseBody());
    }

    @DeleteMapping("/{dataset-id}")
    public void deleteDataset(@AuthenticationPrincipal AuthenticatedUser user,
                              @PathVariable("dataset-id") String datasetId) throws Exception {

        datasetService.deleteDataset(user.getUserId(), datasetId);
    }
}
