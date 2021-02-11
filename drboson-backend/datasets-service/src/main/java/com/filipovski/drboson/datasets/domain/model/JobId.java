package com.filipovski.drboson.datasets.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class JobId extends DomainObjectId {

    protected JobId() {
        super("");
    }

    public JobId(String id) {
        super(id);
    }

    public static JobId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        return new JobId(id);
    }
}
