package com.filipovski.drboson.datasets.domain.model.processing;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class ProcessingId extends DomainObjectId {

    protected ProcessingId() {
        super("");
    }

    public ProcessingId(String id) {
        super(id);
    }

    public static ProcessingId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        return new ProcessingId(id);
    }

}
