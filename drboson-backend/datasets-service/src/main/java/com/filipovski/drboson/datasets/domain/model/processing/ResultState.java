package com.filipovski.drboson.datasets.domain.model.processing;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;

import javax.persistence.Embeddable;

@Embeddable
public class ResultState implements ValueObject {

    private boolean hasResult;

    private boolean aborted;

    protected ResultState() { }

    private ResultState(boolean hasResult, boolean aborted) {
        this.hasResult =  hasResult;
        this.aborted = aborted;
    }

    public boolean hasResult() {
        return hasResult;
    }

    public boolean aborted() {
        return aborted;
    }

    public static ResultState from(boolean hasResult, boolean aborted) {
        return new ResultState(hasResult, aborted);
    }
}
