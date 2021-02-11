package com.filipovski.drboson.runs.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class RunName implements ValueObject {
    private String name;

    protected RunName() { }

    private RunName(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static RunName from(String name) {
        // Imposed constraints. Could be limited to certain format.
        if(Strings.isNullOrEmpty(name))
            throw new IllegalArgumentException("Run's name must not be empty");

        return new RunName(name);
    }
}
