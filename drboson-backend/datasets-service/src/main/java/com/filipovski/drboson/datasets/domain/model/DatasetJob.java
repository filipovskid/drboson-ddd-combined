package com.filipovski.drboson.datasets.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.filipovski.drboson.sharedkernel.domain.base.LocalEntity;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Table(name = "dataset_jobs")
@Entity
public class DatasetJob extends LocalEntity<JobId> {

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Dataset dataset;

    protected DatasetJob() { }

    private DatasetJob(JobType type, Dataset dataset) {
        super(DomainObjectId.randomId(JobId.class));

        this.type = type;
        this.status = JobStatus.PENDING;
        this.dataset = dataset;
    }

    public static DatasetJob createLocalStorageJob(Dataset dataset) {
        return new DatasetJob(JobType.STORAGE, dataset);
    }

    public void updateStatus(String status) {
        JobStatus newStatus = JobStatus.valueOf(status);

        if (!isNewStateValid(newStatus))
            throw new IllegalStateException(String.format("Can't update job state from %s to %s", this.status, newStatus));

        this.status = newStatus;
    }

    public void startJob() {
        updateStatus(JobStatus.PENDING.name());
    }

    private boolean isNewStateValid(JobStatus newStatus) {
        ArrayList<JobStatus> stateOrder = new ArrayList<>(Arrays.asList(
                JobStatus.IDLE, JobStatus.PENDING, JobStatus.RUNNING, JobStatus.COMPLETED, JobStatus.FAILED
        ));

        if((newStatus == JobStatus.IDLE || newStatus == JobStatus.PENDING) && this.status == JobStatus.FAILED)
            return true;

        if(stateOrder.indexOf(this.status) <= stateOrder.indexOf(newStatus))
            return true;

        return false;
    }
}
