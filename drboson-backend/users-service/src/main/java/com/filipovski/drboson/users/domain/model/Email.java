package com.filipovski.drboson.users.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Email implements ValueObject {

    private String email;

    protected Email() {}

    private Email(String email) {
        this.email = email;
    }

    public String email() {
        return email;
    }

    public static Email from(String email) {
        Objects.requireNonNull(email, "email must not be null");
        String cleanEmail = email.trim();

        if(cleanEmail.length() == 0)
            throw new IllegalArgumentException("email must not be empty");

        // Email format validation

        return new Email(cleanEmail);
    }
}
