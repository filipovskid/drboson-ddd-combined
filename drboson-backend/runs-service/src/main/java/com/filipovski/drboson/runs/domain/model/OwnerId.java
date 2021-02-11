package com.filipovski.drboson.runs.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class OwnerId extends DomainObjectId {

    public OwnerId() {
        super("");
    }

    public OwnerId(String id) {
        super(id);
    }

    public static OwnerId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be emppty");

        return new OwnerId(id);
    }
}
