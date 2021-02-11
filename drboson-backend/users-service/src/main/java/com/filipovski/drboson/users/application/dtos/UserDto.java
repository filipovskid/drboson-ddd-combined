package com.filipovski.drboson.users.application.dtos;

import com.filipovski.drboson.users.domain.model.User;
import lombok.Getter;

@Getter
public class UserDto {
    private String username;
    private String email;
    private String name;

    public UserDto(String username, String email, String name) {
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public static UserDto from(User user) {
        return new UserDto(
                user.getUsername().username(),
                user.getEmail().email(),
                user.getName().name()
        );
    }
}
