package com.filipovski.drboson.users.domain.event;

import com.filipovski.drboson.sharedkernel.domain.base.DomainEvent;
import com.filipovski.drboson.users.domain.model.User;
import com.filipovski.drboson.users.domain.model.UserId;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class UserCreatedEvent implements DomainEvent {

    private final UserId userId;

    private final String username;

    private final String password;

    private final String email;

    private final Instant occuredOn;

    public UserCreatedEvent(UserId userId, String username, String password, String email) {
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.username = Objects.requireNonNull(username, "username must not be null");
        this.password = Objects.requireNonNull(password, "password must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.occuredOn = Instant.now();
    }

    public static UserCreatedEvent from(User user) {
        return new UserCreatedEvent(user.id(),
                user.getUsername().username(),
                user.getPassword().password(),
                user.getEmail().email()
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
