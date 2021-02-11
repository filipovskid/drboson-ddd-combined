package com.filipovski.drboson.datasets.domain.model.processing;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class ColumnSchema implements ValueObject {

    private String name;

    private String type;

    protected ColumnSchema() { }

    private ColumnSchema(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }

    public static ColumnSchema from(String name, String type) {

        if(Strings.isNullOrEmpty(name))
            throw new IllegalArgumentException("name must not be empty");

        if(Strings.isNullOrEmpty(type))
            throw new IllegalArgumentException("type must not be empty");


        return new ColumnSchema(name, type);
    }
}
