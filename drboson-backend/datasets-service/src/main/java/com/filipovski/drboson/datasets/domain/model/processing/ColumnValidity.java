package com.filipovski.drboson.datasets.domain.model.processing;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;

import javax.persistence.Embeddable;

@Embeddable
public class ColumnValidity implements ValueObject {

    private int missingValuesCount;

    private int validValuesCount;

    protected ColumnValidity() { }

    private ColumnValidity(int missingValuesCount, int validValuesCount) {
        this.missingValuesCount = missingValuesCount;
        this.validValuesCount = validValuesCount;
    }

    public int missingValuesCount() {
        return missingValuesCount;
    }

    public int validValuesCount() {
        return validValuesCount;
    }

    public static ColumnValidity from(int missingValuesCount, int validValuesCount) {
        if (missingValuesCount < 0 || validValuesCount < 0)
            throw new IllegalArgumentException("Value validity count is invalid");

        return new ColumnValidity(missingValuesCount, validValuesCount);
    }

}
