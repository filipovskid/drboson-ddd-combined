package com.filipovski.drboson.runs.domain.model.files;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class FileId extends DomainObjectId {

    protected FileId() { super(""); }

    public FileId(String id) {
        super(id);
    }

    public static FileId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        return new FileId(id);
    }
}