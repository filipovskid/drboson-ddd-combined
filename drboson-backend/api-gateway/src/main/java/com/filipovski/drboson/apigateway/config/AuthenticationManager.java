package com.filipovski.drboson.apigateway.config;

import com.filipovski.drboson.sharedkernel.security.AuthenticatedUser;
import com.filipovski.drboson.sharedkernel.security.JwtTokenHandler;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenHandler tokenHandler;

    public AuthenticationManager(JwtTokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        try {
            Claims claims = tokenHandler.validateToken(token);

            String username = tokenHandler.claimUsername(claims);
            String userId = tokenHandler.claimUserId(claims);

            if(!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(userId)) {
                AuthenticatedUser user = AuthenticatedUser.from(username, userId);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(auth);
                return Mono.just(auth);
            }
        } catch (Exception e) {
            return Mono.empty();
        }

        return Mono.empty();
    }
}
