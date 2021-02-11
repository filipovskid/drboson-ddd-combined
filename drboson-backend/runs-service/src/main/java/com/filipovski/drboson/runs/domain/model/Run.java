package com.filipovski.drboson.runs.domain.model;

import com.filipovski.drboson.runs.domain.event.RunStartedEvent;
import com.filipovski.drboson.runs.domain.model.files.RunFile;
import com.filipovski.drboson.runs.domain.model.metrics.MetricLog;
import com.filipovski.drboson.sharedkernel.domain.base.AggregateRoot;
import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import lombok.Getter;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Table(name = "runs")
@Entity
public class Run extends AggregateRoot<RunId> {

    @Version
    private Long version;

    @AttributeOverride(name = "id", column = @Column(name = "owner_id", nullable = false))
    @Embedded
    private OwnerId ownerId;

    @AttributeOverride(name = "id", column = @Column(name = "project_id", nullable = false))
    @Embedded
    private ProjectId projectId;

    @Embedded
    private RunName name;

    @Embedded
    private RunDescription description;

    @Embedded
    private GitRepository repository;

    @Enumerated(EnumType.STRING)
    private RunStatus status;

    @ManyToOne
    private Dataset dataset;

    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetricLog> logs;

    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RunFile> files;

    protected Run() { }

    private Run(OwnerId ownerId, ProjectId ownerProjectId, RunName name, RunDescription description,
                GitRepository repository) {
        super(DomainObjectId.randomId(RunId.class));

        this.ownerId = ownerId;
        this.projectId = ownerProjectId;
        this.name = name;
        this.description = description;
        this.status = RunStatus.IDLE;
        this.repository = repository;
    }

    public boolean start() {
        if (status != RunStatus.IDLE && status != RunStatus.FAILED)
            return false;

        status = RunStatus.PENDING;
        this.registerEvent(RunStartedEvent.from(this));

        return true;
    }

    public void updateRunStatus(RunStatus status) {
        if(canGoToState(status))
            this.status = status;
    }

    public void addMetricLog(MetricLog log) {
        Objects.requireNonNull(log, "log must not be null");

        this.logs.add(log);
    }

    public void addRunFile(RunFile file) {
        Objects.requireNonNull(file, "file must not be null");
        files.add(file);
    }

    private boolean canGoToState(RunStatus newStatus) {
        ArrayList<RunStatus> orderedStates = new ArrayList<>(
                Arrays.asList(RunStatus.IDLE, RunStatus.PENDING, RunStatus.RUNNING, RunStatus.COMPLETED, RunStatus.FAILED)
        );

        if(status == RunStatus.COMPLETED || status == RunStatus.FAILED)
            return false;

        return orderedStates.indexOf(newStatus) > orderedStates.indexOf(status);
    }

    public static RunBuilder builder() {
        return new RunBuilder();
    }

    public static class RunBuilder {
        private OwnerId ownerId;
        private ProjectId ownerProjectId;
        private RunName name;
        private RunDescription description;
        private String repository;
        private Dataset dataset;

        private RunBuilder() {}

        public RunBuilder ownerId(String id) {
            this.ownerId = OwnerId.from(id);
            return this;
        }

        public RunBuilder name(String name) {
            this.name = RunName.from(name);
            return this;
        }

        public RunBuilder description(String description) {
            this.description = RunDescription.from(description);
            return this;
        }

        public RunBuilder repository(String repository) {
            this.repository = repository;
            return this;
        }

        public RunBuilder projectId(String id) {
            this.ownerProjectId = ProjectId.from(id);
            return this;
        }

        public RunBuilder dataset(Dataset dataset) {
            this.dataset = dataset;
            return this;
        }

        public Run build() throws MalformedURLException {
            Objects.requireNonNull(ownerId, "ownerId must not be null");
            Objects.requireNonNull(name, "name must not be null");
            Objects.requireNonNull(description, "description  must not be null");
            Objects.requireNonNull(ownerProjectId, "projectId  must not be null");
            Objects.requireNonNull(dataset, "Dataset must not be null");

            Run run = new Run(ownerId, ownerProjectId, name, description, GitRepository.from(repository));
            run.dataset = dataset;

            return run;
        }
    }
}
