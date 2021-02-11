package com.filipovski.drboson.datasets.application.dtos.processing;

import com.filipovski.drboson.datasets.domain.model.processing.ProcessingColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ColumnHeader {
    private String id;
    private String name;
    private String type;
    private int noMissingValues;
    private int noOK;

    public static ColumnHeader from(ProcessingColumn column) {
        return new ColumnHeader(
                column.id().getId(),
                column.getColumnSchema().name(),
                column.getColumnSchema().type(),
                column.getColumnValidity().missingValuesCount(),
                column.getColumnValidity().validValuesCount()
        );
    }
}
