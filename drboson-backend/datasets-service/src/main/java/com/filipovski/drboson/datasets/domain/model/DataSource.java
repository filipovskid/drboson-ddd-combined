package com.filipovski.drboson.datasets.domain.model;

import com.filipovski.drboson.datasets.utils.FileUtils;
import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Embeddable
public class DataSource implements ValueObject {
    private String location;

    @Enumerated(value = EnumType.STRING)
    private DataType type;

    private String MIMEType;

    protected DataSource() { }

    private DataSource(String location, DataType type, String MIMEType) {
        this.location = location;
        this.type = type;
        this.MIMEType = MIMEType;
    }

    public static DataSource from(String location, String mimeType) {
        if(Strings.isNullOrEmpty(mimeType))
            throw new IllegalArgumentException("MIMEType must not be empty");

        if(Strings.isNullOrEmpty(location))
            throw new IllegalArgumentException("location must not be empty");

        if(FileUtils.rdfMIMETypes.contains(mimeType))
            return new DataSource(location, DataType.RDF, mimeType);

        if(FileUtils.commonMIMETypes.contains(mimeType))
            return new DataSource(location, DataType.RDF, mimeType);

        throw new IllegalArgumentException(String.format("MIMEType <%s> not supported", mimeType));
    }

    public static String generateLocation(String key) {
        if (Strings.isNullOrEmpty(key))
            return UUID.randomUUID().toString();

        return String.format("%s--%s", UUID.randomUUID(), key);
    }

    public String location() {
        return location;
    }

    public DataType type() {
        return type;
    }

    public String mimeType() {
        return MIMEType;
    }

}
