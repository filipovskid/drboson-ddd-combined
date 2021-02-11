package com.filipovski.drboson.datasets.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class OwnerProjectId extends DomainObjectId {

    protected OwnerProjectId() {
        super("");
    }

    public OwnerProjectId(String id) {
        super(id);
    }

    public static OwnerProjectId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        return new OwnerProjectId(id);
    }
}
