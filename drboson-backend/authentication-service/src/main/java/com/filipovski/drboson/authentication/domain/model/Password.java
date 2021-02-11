package com.filipovski.drboson.authentication.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;
import com.google.common.base.Strings;
import org.springframework.util.Assert;

import javax.persistence.Embeddable;

@Embeddable
public class Password implements ValueObject {
    private String password;
    private boolean erased;

    protected Password() {}

    public String password() {
        return password;
    }

    public boolean isErased() {
        return erased;
    }

    private Password(String password) {
        setPassword(password);
    }

    public void erase() {
        this.password = null;
        this.erased = true;
    }

    public static Password from(String password) {
        return new Password(password);
    }

    private void setPassword(String password) {
        if(Strings.isNullOrEmpty(password)) {
            this.password = null;
            this.erased = true;
        }

        this.password = password;
        this.erased = false;
    }
}
