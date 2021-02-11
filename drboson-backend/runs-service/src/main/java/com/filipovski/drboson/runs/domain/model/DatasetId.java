package com.filipovski.drboson.runs.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class DatasetId extends DomainObjectId {

    public DatasetId() {
        super("");
    }

    public DatasetId(String id) {
        super(id);
    }

    public static DatasetId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        return new DatasetId(id);
    }
}
