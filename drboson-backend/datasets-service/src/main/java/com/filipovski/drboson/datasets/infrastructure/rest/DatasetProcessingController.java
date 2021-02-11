package com.filipovski.drboson.datasets.infrastructure.rest;

import com.filipovski.drboson.datasets.application.DatasetJobService;
import com.filipovski.drboson.datasets.application.DatasetProcessingService;
import com.filipovski.drboson.datasets.application.dtos.DatasetDto;
import com.filipovski.drboson.datasets.application.dtos.processing.RefreshColumnsResponse;
import com.filipovski.drboson.datasets.domain.model.Dataset;
import com.filipovski.drboson.sharedkernel.security.AuthenticatedUser;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dataset", produces = MediaType.APPLICATION_JSON_VALUE)
public class DatasetProcessingController {

    private final DatasetJobService jobService;
    private final DatasetProcessingService processingService;

    public DatasetProcessingController(DatasetJobService jobService,
                                       DatasetProcessingService processingService) {
        this.jobService = jobService;
        this.processingService = processingService;
    }

    @GetMapping("{dataset-id}/local-storage")
    public ResponseEntity<DatasetDto> storeDatasetLocally(@AuthenticationPrincipal AuthenticatedUser user,
                                                          @PathVariable("dataset-id") String datasetId) throws Exception {

        return ResponseEntity.ok(jobService.storeDatasetLocally(user.getUserId(), datasetId));
    }

    @GetMapping("{dataset-id}/refresh-columns")
    public ResponseEntity<RefreshColumnsResponse> refreshColumns(@AuthenticationPrincipal AuthenticatedUser user,
                                                                 @PathVariable("dataset-id") String datasetId) throws Exception {
        return ResponseEntity.ok(processingService.getProcessingState(user.getUserId(), datasetId));
    }
}
