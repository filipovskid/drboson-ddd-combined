package com.filipovski.drboson.runs.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class RunDescription implements ValueObject {
    private String description;

    protected RunDescription() { }

    private RunDescription(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    public static RunDescription from(String description) {
        if(Strings.isNullOrEmpty(description))
            return new RunDescription("");

        return new RunDescription(description);
    }
}
