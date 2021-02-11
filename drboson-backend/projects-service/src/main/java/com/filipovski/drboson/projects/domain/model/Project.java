package com.filipovski.drboson.projects.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.AggregateRoot;
import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import lombok.Getter;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.util.Objects;

@Getter
@Entity
@Table(name = "projects")
public class Project extends AggregateRoot<ProjectId> {

    @Version
    private Long version;

    @AttributeOverride(name = "id", column = @Column(name = "owner_id", nullable = false))
    @Embedded
    private OwnerId ownerId;

    @Embedded
    private ProjectName name;

    @Embedded
    private ProjectDescription description;

    @Embedded
    private GitRepository repository;

    protected Project() { }

    private Project(OwnerId ownerId, ProjectName name, ProjectDescription description, GitRepository repo) {
        super(DomainObjectId.randomId(ProjectId.class));
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.repository = repo;
    }

    public static Project from(String ownerId, String name, String description, String repositoryUrl)
            throws MalformedURLException {
        Objects.requireNonNull(ownerId, "ownerId must not be null");

        return new Project(
                new OwnerId(ownerId),
                ProjectName.from(name),
                ProjectDescription.from(description),
                GitRepository.from(repositoryUrl)
        );
    }
}
