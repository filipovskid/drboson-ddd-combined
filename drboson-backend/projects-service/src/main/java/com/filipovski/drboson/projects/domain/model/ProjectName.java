package com.filipovski.drboson.projects.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
public class ProjectName implements ValueObject {
    private String name;

    protected ProjectName() { }

    private ProjectName(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static ProjectName from(String name) {
        // Imposed constraints. Could have eg. limited to a certain format or set of characters.
        if(Strings.isNullOrEmpty(name))
            throw new IllegalArgumentException("Project's name must not be empty");

        return new ProjectName(name);
    }
}
