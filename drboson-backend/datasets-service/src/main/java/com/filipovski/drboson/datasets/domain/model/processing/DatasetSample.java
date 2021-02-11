package com.filipovski.drboson.datasets.domain.model.processing;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class DatasetSample implements ValueObject {

    private String name;

    protected DatasetSample() { }

    private DatasetSample(String name) {
        this.name = name;
    }

    public static DatasetSample from(String name) {
        if(Strings.isNullOrEmpty(name))
            throw new IllegalArgumentException("Sample name must not be empty");

        return new DatasetSample(name);
    }

}
