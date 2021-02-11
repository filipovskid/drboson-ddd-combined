package com.filipovski.drboson.runs.domain.model.files;

import com.filipovski.drboson.runs.domain.model.DataSource;
import com.filipovski.drboson.runs.domain.model.Run;
import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.filipovski.drboson.sharedkernel.domain.base.LocalEntity;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Table(name = "run_files")
@Entity
public class RunFile extends LocalEntity<FileId> {

    @Version
    private Long version;

    @Embedded
    private FileName name;

    @Embedded
    private DataSource dataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    private Run run;

    protected RunFile() { }

    private RunFile(Run run, FileName name, DataSource dataSource) {
        super(DomainObjectId.randomId(FileId.class));

        this.run = run;
        this.name = name;
        this.dataSource = dataSource;
    }

    public static RunFile from(Run run, FileName name, DataSource dataSource) {
        Objects.requireNonNull(run, "Run must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(dataSource, "dataSource must not be null");

        return new RunFile(run, name, dataSource);
    }

}
