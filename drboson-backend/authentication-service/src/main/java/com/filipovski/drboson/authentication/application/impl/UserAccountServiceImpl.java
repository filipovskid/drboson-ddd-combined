package com.filipovski.drboson.authentication.application.impl;

import com.filipovski.drboson.authentication.application.UserAccountService;
import com.filipovski.drboson.authentication.application.dtos.AuthenticationRequest;
import com.filipovski.drboson.authentication.application.dtos.AuthenticationResponse;
import com.filipovski.drboson.authentication.domain.event.UserCreatedEvent;
import com.filipovski.drboson.authentication.domain.model.UserAccount;
import com.filipovski.drboson.authentication.domain.model.Username;
import com.filipovski.drboson.authentication.domain.repository.UserAccountRepository;

import com.filipovski.drboson.authentication.security.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository,
                                  AuthenticationManager authenticationManager,
                                  JwtProvider jwtProvider) {
        this.userAccountRepository = userAccountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createUserAccount(UserCreatedEvent user) {
        UserAccount account = UserAccount.username(user.getUsername())
                .userId(user.getUserId().getId())
                .password(user.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

        userAccountRepository.save(account);
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        UserAccount user = userAccountRepository.findByUsername(Username.from(request.getUsername()))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found!", request.getUsername())));

        return new AuthenticationResponse(jwtProvider.generateToken(user));
    }
}
