package com.filipovski.drboson.apigateway.config;

import com.filipovski.drboson.sharedkernel.security.JwtTokenHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenHandler tokenHandler;

    public SecurityContextRepository(AuthenticationManager authenticationManager,
                                     JwtTokenHandler tokenHandler) {
        this.authenticationManager = authenticationManager;
        this.tokenHandler = tokenHandler;
    }

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        String token = tokenHandler.resolveToken(serverWebExchange.getRequest());

        if(token == null)
            return Mono.empty();

        Authentication auth = new UsernamePasswordAuthenticationToken(token, token);

        return this.authenticationManager.authenticate(auth)
                .map(SecurityContextImpl::new);
    }
}
