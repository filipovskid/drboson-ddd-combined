package com.filipovski.drboson.users.application.validation;

import com.filipovski.drboson.users.application.dtos.UserRegistrationRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        UserRegistrationRequest user = (UserRegistrationRequest) o;
        return user.getPassword().equals(user.getPasswordConfirmation());
    }
}
