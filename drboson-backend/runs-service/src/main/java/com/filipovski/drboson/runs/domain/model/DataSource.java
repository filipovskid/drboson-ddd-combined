package com.filipovski.drboson.runs.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class DataSource implements ValueObject {

    private String location;

    private String MIMEType;

    protected DataSource() { }

    private DataSource(String location, String MIMEType) {
        this.location = location;
        this.MIMEType = MIMEType;
    }

    public String location() {
        return location;
    }

    public String mimeType() {
        return MIMEType;
    }

    public static DataSource from(String location, String mimeType) {
        if(Strings.isNullOrEmpty(location))
            throw new IllegalArgumentException("Data source location must not be empty");

        if(Strings.isNullOrEmpty(mimeType))
            throw new IllegalArgumentException("MIMEType must not be empty");

        return new DataSource(location, mimeType);
    }
}
