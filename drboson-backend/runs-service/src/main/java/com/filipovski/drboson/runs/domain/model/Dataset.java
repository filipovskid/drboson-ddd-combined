package com.filipovski.drboson.runs.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.filipovski.drboson.sharedkernel.domain.base.LocalEntity;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Entity
public class Dataset extends LocalEntity<DatasetId> {

    @Version
    private Long version;

    @AttributeOverride(name = "id", column = @Column(name = "owner_id", nullable = false))
    @Embedded
    private OwnerId ownerId;

    @AttributeOverride(name = "id", column = @Column(name = "project_id", nullable = false))
    @Embedded
    private ProjectId projectId;

    @AttributeOverrides({
            @AttributeOverride(name = "location", column = @Column(name = "data_source_location")),
            @AttributeOverride(name = "MIMEType", column = @Column(name = "data_MIME_type"))
    })
    @Embedded
    private DataSource dataSource;

    protected Dataset() { }

    private Dataset(DatasetId datasetId, OwnerId ownerId, ProjectId projectId, DataSource source) {
        super(datasetId);

        this.ownerId = ownerId;
        this.projectId = projectId;
        this.dataSource = source;
    }

    public static DatasetBuilder builder() {
        return new DatasetBuilder();
    }

    public static class DatasetBuilder {
        private DatasetId datasetId;
        private OwnerId ownerId;
        private ProjectId projectId;
        private String location;
        private String mimeType;

        private DatasetBuilder() {}

        public DatasetBuilder datasetId(String id) {
            this.datasetId = DatasetId.from(id);
            return this;
        }

        public DatasetBuilder ownerId(String id) {
            this.ownerId = OwnerId.from(id);
            return this;
        }

        public DatasetBuilder projectId(String id) {
            this.projectId = ProjectId.from(id);
            return this;
        }

        public DatasetBuilder location(String location) {
            this.location = location;
            return this;
        }

        public DatasetBuilder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Dataset build() {
            Objects.requireNonNull(datasetId, "datasetId must not be null");
            Objects.requireNonNull(ownerId, "ownerId must not be null");
            Objects.requireNonNull(projectId, "projectId  must not be null");
            Objects.requireNonNull(location, "location  must not be null");
            Objects.requireNonNull(mimeType, "mimeType must not be null");
            DataSource dataSource = DataSource.from(location, mimeType);

            return new Dataset(datasetId, ownerId, projectId, dataSource);
        }
    }

}
