package com.filipovski.drboson.datasets.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class DatasetName implements ValueObject {
    private String name;

    protected DatasetName() { }

    private DatasetName(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static DatasetName from(String name) {
        // Imposed constraints. Could have eg. limited to a certain format or set of characters.
        if(Strings.isNullOrEmpty(name))
            throw new IllegalArgumentException("Dataset's name must not be empty");

        return new DatasetName(name);
    }
}
