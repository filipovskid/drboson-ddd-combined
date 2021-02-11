package com.filipovski.drboson.users.application.dtos;

import com.filipovski.drboson.users.application.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserRegistrationRequest {
    @NotEmpty(message = "Username must not be empty")
    private String username;

    @ValidEmail
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    private String password;
    private String passwordConfirmation;
}
