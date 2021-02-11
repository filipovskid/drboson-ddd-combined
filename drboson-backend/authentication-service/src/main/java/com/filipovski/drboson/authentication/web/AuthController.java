package com.filipovski.drboson.authentication.web;

import com.filipovski.drboson.authentication.application.UserAccountService;
import com.filipovski.drboson.authentication.application.dtos.AuthenticationRequest;
import com.filipovski.drboson.authentication.application.dtos.AuthenticationResponse;
import com.filipovski.drboson.sharedkernel.security.AuthenticatedUser;
import com.filipovski.drboson.sharedkernel.security.JwtConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

@RestController
@RequestMapping(path = "/auth", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class AuthController {
    private final UserAccountService userAccountService;
    private final JwtConfig jwtConfig;

    public AuthController(UserAccountService userAccountService, JwtConfig jwtConfig) {
        this.userAccountService = userAccountService;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request,
                                                        HttpServletResponse response) {
        AuthenticationResponse authResponse = userAccountService.login(request);

        Cookie cookie = new Cookie(jwtConfig.getCookieName(), authResponse.getJwt());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(String.format("Username: %s, Id: %s", user.getUsername(), user.getUserId()));
    }
}
