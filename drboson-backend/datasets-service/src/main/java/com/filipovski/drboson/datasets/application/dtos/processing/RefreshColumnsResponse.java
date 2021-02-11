package com.filipovski.drboson.datasets.application.dtos.processing;

import com.filipovski.drboson.datasets.domain.model.processing.DataProcessing;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RefreshColumnsResponse {
    private boolean hasResult;
    private List<ColumnHeader> headers;

    public static RefreshColumnsResponse from(DataProcessing processing) {
        List<ColumnHeader> columnHeaders = processing.getColumns().stream()
                .map(ColumnHeader::from)
                .collect(Collectors.toList());

        RefreshColumnsResponse response = new RefreshColumnsResponse(
                processing.getResultState().hasResult(),
                columnHeaders
        );

        return response;
    }
}
