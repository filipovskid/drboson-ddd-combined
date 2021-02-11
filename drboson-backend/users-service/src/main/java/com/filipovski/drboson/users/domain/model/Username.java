package com.filipovski.drboson.users.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Username implements ValueObject {
    private String username;

    protected Username() {}

    private Username(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }

    public static Username from(String username) {
        Objects.requireNonNull(username, "username must not be null");
        String cleanUsername = username.trim().toLowerCase();

        if(cleanUsername.length() == 0)
            throw new IllegalArgumentException("username must not be empty");

        if(cleanUsername.contains(" "))
            throw new IllegalArgumentException("username must not contain whitespaces");

        return new Username(cleanUsername);
    }

}
