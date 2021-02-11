package com.filipovski.drboson.authentication.domain.repository;

import com.filipovski.drboson.authentication.domain.model.UserAccount;
import com.filipovski.drboson.authentication.domain.model.UserAccountId;
import com.filipovski.drboson.authentication.domain.model.Username;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, UserAccountId> {
    Optional<UserAccount> findByUsername(Username username);
}
