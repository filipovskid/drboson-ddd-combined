package com.filipovski.drboson.users.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;

import javax.persistence.Embeddable;

@Embeddable
public class Password implements ValueObject {
    private String password;

    protected Password() {}

    public String password() {
        return password;
    }

    private Password(String password) {
        this.password = password;
    }

    public static Password from(String password) {
        return new Password(password);
    }
}
