package com.filipovski.drboson.users.domain.repository;

import com.filipovski.drboson.users.domain.model.User;
import com.filipovski.drboson.users.domain.model.UserId;
import com.filipovski.drboson.users.domain.model.Username;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
    Optional<User> findUserByUsername(Username username);
}
