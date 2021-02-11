package com.filipovski.drboson.datasets.application.dtos;

import com.filipovski.drboson.datasets.domain.model.Dataset;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Getter
@AllArgsConstructor
public class DownloadDatasetResponse {
    private String id;
    private String projectId;
    private String name;
    private String description;
    private String MIMEType;
    private StreamingResponseBody responseBody;

    public static DownloadDatasetResponse from(Dataset dataset, StreamingResponseBody responseBody) {
        return new DownloadDatasetResponse(
                dataset.id().getId(),
                dataset.getOwnerProjectId().getId(),
                dataset.getName().name(),
                dataset.getDescription().description(),
                dataset.getDataSource().mimeType(),
                responseBody
        );
    }
}
