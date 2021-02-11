package com.filipovski.drboson.runs.application.dtos;

import com.filipovski.drboson.runs.domain.model.files.RunFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Getter
@AllArgsConstructor
public class DownloadRunFileResponse {
    private String id;
    private String name;
    private String mimeType;
    private StreamingResponseBody responseBody;

    public static DownloadRunFileResponse from(RunFile file, StreamingResponseBody responseBody) {
        return new DownloadRunFileResponse(
                file.id().getId(),
                file.getName().name(),
                file.getDataSource().mimeType(),
                responseBody
        );
    }
}
