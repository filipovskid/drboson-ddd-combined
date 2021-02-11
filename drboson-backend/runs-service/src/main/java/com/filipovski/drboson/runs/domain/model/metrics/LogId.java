package com.filipovski.drboson.runs.domain.model.metrics;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class LogId extends DomainObjectId {

    protected LogId() { super(""); }

    public LogId(String id) {
        super(id);
    }

    public static LogId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        return new LogId(id);
    }
}
