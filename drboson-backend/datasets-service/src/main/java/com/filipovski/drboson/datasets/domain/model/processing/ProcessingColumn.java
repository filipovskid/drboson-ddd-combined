package com.filipovski.drboson.datasets.domain.model.processing;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.filipovski.drboson.sharedkernel.domain.base.LocalEntity;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Table(name = "columns")
@Entity
public class ProcessingColumn extends LocalEntity<ColumnId> {

    @Version
    private Long version;

    @Embedded
    private ColumnSchema columnSchema;

    @Embedded
    private ColumnValidity columnValidity;

    @ManyToOne(fetch = FetchType.LAZY)
    private DataProcessing processing;

    protected ProcessingColumn() { }

    private ProcessingColumn(ColumnSchema columnSchema, ColumnValidity columnValidity) {
        super(DomainObjectId.randomId(ColumnId.class));

        this.columnSchema = columnSchema;
        this.columnValidity = columnValidity;
    }

    public void addDataProcessing(DataProcessing processing) {
        Objects.requireNonNull(processing, "processing must not be null");

        this.processing = processing;
    }

    public static ProcessingColumn from(ColumnSchema columnSchema, ColumnValidity columnValidity) {
        Objects.requireNonNull(columnSchema, "columnSchema must noe be null");
        Objects.requireNonNull(columnValidity, "columnValidity must noe be null");

        return new ProcessingColumn(columnSchema, columnValidity);
    }

}
