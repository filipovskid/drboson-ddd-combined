package com.filipovski.drboson.users.application;

import com.filipovski.drboson.users.application.dtos.UserDto;
import com.filipovski.drboson.users.application.dtos.UserRegistrationRequest;
import com.filipovski.drboson.users.domain.model.User;

public interface UserService {
    User registerNewUserAccount(UserRegistrationRequest user);

    UserDto getUser(String username) throws Exception;
}
