package com.filipovski.drboson.projects.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;
import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class ProjectDescription implements ValueObject {
    private String description;

    protected ProjectDescription() { }

    private ProjectDescription(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    public static ProjectDescription from(String description) {
        if(Strings.isNullOrEmpty(description))
            return new ProjectDescription("");

        return new ProjectDescription(description);
    }
}
