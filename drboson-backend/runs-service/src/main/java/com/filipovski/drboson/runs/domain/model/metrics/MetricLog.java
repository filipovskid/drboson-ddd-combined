package com.filipovski.drboson.runs.domain.model.metrics;

import com.filipovski.drboson.runs.domain.model.ProjectId;
import com.filipovski.drboson.runs.domain.model.Run;
import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.filipovski.drboson.sharedkernel.domain.base.LocalEntity;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Table(name = "metric_logs")
@Entity
public class MetricLog extends LocalEntity<LogId> {

    @Version
    private Long version;

    @Embedded
    private LogData log;

    @ManyToOne(fetch = FetchType.LAZY)
    private Run run;

    protected MetricLog() { }

    private MetricLog(Run run, LogData log) {
        super(DomainObjectId.randomId(LogId.class));

        this.run = run;
        this.log = log;
    }

    public static MetricLog from(Run run, LogData log) {
        Objects.requireNonNull(run, "Run must not be null");
        Objects.requireNonNull(log, "log must not be null");

        return new MetricLog(run, log);
    }

}
