package com.filipovski.drboson.users.application.impl;

import com.filipovski.drboson.users.application.UserService;
import com.filipovski.drboson.users.application.dtos.UserDto;
import com.filipovski.drboson.users.application.dtos.UserRegistrationRequest;
import com.filipovski.drboson.users.domain.model.User;
import com.filipovski.drboson.users.domain.model.Username;
import com.filipovski.drboson.users.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUserAccount(UserRegistrationRequest user) {
        User registeringUser = User.withUsername(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .name(user.getUsername())
                .passwordEncoder(passwordEncoder::encode)
                .build();

//        Events published after persisting the object
        return userRepository.save(registeringUser);
    }

    @Override
    public UserDto getUser(String username) throws Exception {
        User user = userRepository.findUserByUsername(Username.from(username))
                .orElseThrow(() -> new Exception("User not found"));

        return UserDto.from(user);
    }
}
