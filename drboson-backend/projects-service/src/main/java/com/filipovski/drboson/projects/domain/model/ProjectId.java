package com.filipovski.drboson.projects.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class ProjectId extends DomainObjectId {

    public ProjectId() {
        super("");
    }

    public ProjectId(String id) {
        super(id);
    }

    public static ProjectId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be emppty");

        return new ProjectId(id);
    }
}
