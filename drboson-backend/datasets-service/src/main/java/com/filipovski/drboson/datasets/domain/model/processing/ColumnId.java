package com.filipovski.drboson.datasets.domain.model.processing;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class ColumnId extends DomainObjectId {

    protected ColumnId() {
        super("");
    }

    public ColumnId(String id) {
        super(id);
    }

    public ColumnId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        return new ColumnId(id);
    }
}
