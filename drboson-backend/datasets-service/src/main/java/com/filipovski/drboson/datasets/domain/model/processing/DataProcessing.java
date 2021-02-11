package com.filipovski.drboson.datasets.domain.model.processing;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.filipovski.drboson.sharedkernel.domain.base.LocalEntity;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Table(name = "data_processing")
@Entity
public class DataProcessing extends LocalEntity<ProcessingId> {

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus status;

    @Embedded
    private ResultState resultState;

    @OneToMany(mappedBy = "processing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessingColumn> columns;

    @AttributeOverride(name = "name", column = @Column(name = "sample_name"))
    @Embedded
    private DatasetSample sample;
    
    protected DataProcessing() { }

    private DataProcessing(ResultState resultState, List<ProcessingColumn> columns) {
        super(DomainObjectId.randomId(ProcessingId.class));

        this.status = ProcessingStatus.IDLE;
        this.resultState = resultState;
        this.columns = columns;
    }

    public void initializeProcessing(List<ProcessingColumn> columns, DatasetSample sample, ResultState resultState) {
        Objects.requireNonNull(columns, "columns must not be null");
        columns.forEach(column -> column.addDataProcessing(this));
        this.columns = columns;

        this.sample = Objects.requireNonNull(sample, "sample must not be null");
        this.resultState = Objects.requireNonNull(resultState, "resultState must not be null");

        this.status = ProcessingStatus.COMPLETED;
    }

    public static ProcessingBuilder builder() {
        return new ProcessingBuilder();
    }

    public static class ProcessingBuilder {
        private boolean hasResult = false;
        private boolean aborted = false;
        private List<ProcessingColumn> columns = new ArrayList<>();

        private ProcessingBuilder() { }

        public ProcessingBuilder hasResult(boolean hasResult) {
            this.hasResult = hasResult;
            return this;
        }

        public ProcessingBuilder hasAborted(boolean aborted) {
            this.aborted = aborted;
            return this;
        }

        public ProcessingBuilder processingColumns(List<ProcessingColumn> columns) {
            this.columns = columns;
            return this;
        }

        public DataProcessing build() {
            return new DataProcessing(
                    ResultState.from(hasResult, aborted),
                    columns
            );
        }
    }

}
