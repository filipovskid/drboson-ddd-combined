package com.filipovski.drboson.datasets.domain.model;

import com.filipovski.drboson.datasets.domain.event.DatasetCreatedEvent;
import com.filipovski.drboson.datasets.domain.event.DatasetStorageJobEvent;
import com.filipovski.drboson.datasets.domain.model.processing.DataProcessing;
import com.filipovski.drboson.datasets.domain.model.processing.DatasetSample;
import com.filipovski.drboson.datasets.domain.model.processing.ProcessingColumn;
import com.filipovski.drboson.datasets.domain.model.processing.ResultState;
import com.filipovski.drboson.sharedkernel.domain.base.AggregateRoot;
import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Getter
@Table(name = "datasets")
@Entity
public class Dataset extends AggregateRoot<DatasetId> {

    @Version
    private Long version;

    @AttributeOverride(name = "id", column = @Column(name = "owner_id", nullable = false))
    @Embedded
    private OwnerId ownerId;

    @AttributeOverride(name = "id", column = @Column(name = "owner_project_id", nullable = false))
    @Embedded
    private OwnerProjectId ownerProjectId;

    @Embedded
    private DatasetName name;

    @Embedded
    private DatasetDescription description;

    @Embedded
    private DataSource dataSource;

    @AttributeOverrides({
            @AttributeOverride(name = "source_mimetype", column = @Column(name = "local_source_mimetype")),
            @AttributeOverride(name = "source_location", column = @Column(name = "local_source_location")),
            @AttributeOverride(name = "type", column = @Column(name = "local_storage_type"))
    })
    @Embedded
    private DataStorage localDataStorage;

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DatasetJob> jobs;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dataset_id")
    private DataProcessing processing;

    protected Dataset() { }

    private Dataset(OwnerId ownerId, OwnerProjectId ownerProjectId, DatasetName name, DatasetDescription description,
                    DataSource source) {
        super(DomainObjectId.randomId(DatasetId.class));

        this.ownerId = ownerId;
        this.ownerProjectId = ownerProjectId;
        this.name = name;
        this.description = description;
        this.dataSource = source;
        this.localDataStorage = DataStorage.createStorate(DataStorageType.LOCAL);
    }

    public void storeDatasetLocally() {
        if(localDataStorage.isPending())
            return;

        DatasetJob job = DatasetJob.createLocalStorageJob(this);
        jobs.add(job);

        localDataStorage = DataStorage.initiateStorage(DataStorageType.LOCAL);
        registerEvent(DatasetStorageJobEvent.from(this, job));
    }

    private boolean activeStorageJobExists() {
        return jobs.stream().anyMatch(job -> job.getType() == JobType.STORAGE &&
                (job.getStatus() != JobStatus.IDLE || job.getStatus() != JobStatus.FAILED));
    }

    public void addLocalStorage(DataStorage storage) {
        this.localDataStorage = storage;
    }

    public void createProcessing(DataProcessing processing) {
        this.processing = processing;
    }

    public StorageStatus localStorageStatus() {
        return localDataStorage.status();
    }

    public void initializeProcessing(List<ProcessingColumn> columns, DatasetSample sample, ResultState resultState) {
        processing.initializeProcessing(columns, sample, resultState);
    }

    public static DatasetBuilder builder() {
        return new DatasetBuilder();
    }

    public static class DatasetBuilder {
        private OwnerId ownerId;
        private OwnerProjectId ownerProjectId;
        private DatasetName name;
        private DatasetDescription description;
        private DataSource dataSource;

        private DatasetBuilder() {}

        public DatasetBuilder ownerId(String id) {
            this.ownerId = OwnerId.from(id);
            return this;
        }

        public DatasetBuilder name(String name) {
            this.name = DatasetName.from(name);
            return this;
        }

        public DatasetBuilder description(String description) {
            this.description = DatasetDescription.from(description);
            return this;
        }

        public DatasetBuilder ownerProjectId(String id) {
            this.ownerProjectId = OwnerProjectId.from(id);
            return this;
        }

        public DatasetBuilder dataSource(DataSource source) {
            this.dataSource = source;
            return this;
        }

        public Dataset build() {
            Objects.requireNonNull(ownerId, "ownerId must not be null");
            Objects.requireNonNull(name, "name must not be null");
            Objects.requireNonNull(description, "description  must not be null");
            Objects.requireNonNull(dataSource, "DataSource must not be null");
            Objects.requireNonNull(ownerProjectId, "ownerProjectId  must not be null");

            Dataset dataset = new Dataset(ownerId, ownerProjectId, name, description, dataSource);
            dataset.registerEvent(DatasetCreatedEvent.from(dataset));

            return dataset;
        }
    }
}
