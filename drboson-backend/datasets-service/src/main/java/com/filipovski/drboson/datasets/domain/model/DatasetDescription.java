package com.filipovski.drboson.datasets.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class DatasetDescription implements ValueObject {
    private String description;

    protected DatasetDescription() { }

    private DatasetDescription(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    public static DatasetDescription from(String description) {
        if(Strings.isNullOrEmpty(description))
            return new DatasetDescription("");

        return new DatasetDescription(description);
    }
}
