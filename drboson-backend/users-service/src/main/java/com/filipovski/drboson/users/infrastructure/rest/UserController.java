package com.filipovski.drboson.users.infrastructure.rest;


import com.filipovski.drboson.sharedkernel.security.AuthenticatedUser;
import com.filipovski.drboson.users.application.UserService;
import com.filipovski.drboson.users.application.dtos.UserDto;
import com.filipovski.drboson.users.application.dtos.UserRegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewUserAccount(@RequestBody UserRegistrationRequest user) {
        userService.registerNewUserAccount(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal AuthenticatedUser user) throws Exception {
        return ResponseEntity.ok(
                userService.getUser(user.getUsername())
        );
    }
}
