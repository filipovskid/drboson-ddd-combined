package com.filipovski.drboson.runs.domain.model.metrics;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class LogData implements ValueObject {

    private String log;

    protected LogData() { }

    private LogData(String log) {
        this.log = log;
    }

    public String log() {
        return log;
    }

    public static LogData from(String log) {
        if (Strings.isNullOrEmpty(log))
            throw new IllegalArgumentException("log must not be empty");

        return new LogData(log);
    }
}
