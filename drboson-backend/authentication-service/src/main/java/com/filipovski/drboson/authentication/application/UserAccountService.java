package com.filipovski.drboson.authentication.application;

import com.filipovski.drboson.authentication.application.dtos.AuthenticationRequest;
import com.filipovski.drboson.authentication.application.dtos.AuthenticationResponse;
import com.filipovski.drboson.authentication.domain.event.UserCreatedEvent;

import javax.servlet.http.HttpServletResponse;

public interface UserAccountService {

    void createUserAccount(UserCreatedEvent user);

    AuthenticationResponse login(AuthenticationRequest request);
}
