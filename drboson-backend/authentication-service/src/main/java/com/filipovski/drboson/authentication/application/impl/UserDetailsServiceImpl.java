package com.filipovski.drboson.authentication.application.impl;

import com.filipovski.drboson.authentication.domain.model.Username;
import com.filipovski.drboson.authentication.domain.repository.UserAccountRepository;
import com.filipovski.drboson.authentication.security.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public UserDetailsServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(Username.from(username))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found!", username)));
    }
}
