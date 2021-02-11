package com.filipovski.drboson.runs.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class RunId extends DomainObjectId {

    protected RunId() {
        super("");
    }

    public RunId(String id) {
        super(id);
    }

    public static RunId from(String id)  {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        return new RunId(id);
    }
}
