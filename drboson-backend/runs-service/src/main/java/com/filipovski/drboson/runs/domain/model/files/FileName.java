package com.filipovski.drboson.runs.domain.model.files;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;

import javax.persistence.Embeddable;

@Embeddable
public class FileName implements ValueObject {
    private String name;

    protected FileName() { }

    private FileName(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static FileName from(String name) {
        return new FileName(name);
    }
}
