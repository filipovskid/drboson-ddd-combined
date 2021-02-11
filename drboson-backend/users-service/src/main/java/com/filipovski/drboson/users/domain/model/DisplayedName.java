package com.filipovski.drboson.users.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;
import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;

@Embeddable
public class DisplayedName implements ValueObject {
    private String name;

    protected DisplayedName() {}

    private DisplayedName(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static DisplayedName from(String name) {
        if(Strings.isNullOrEmpty(name))
            throw new IllegalArgumentException("name must not be empty");

        String displayName = StringUtils.capitalize(name.trim());

        return new DisplayedName(displayName);
    }
}
