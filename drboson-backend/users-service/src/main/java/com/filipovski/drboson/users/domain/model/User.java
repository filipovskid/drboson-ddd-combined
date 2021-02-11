package com.filipovski.drboson.users.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.AggregateRoot;
import com.filipovski.drboson.users.domain.event.UserCreatedEvent;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.function.Function;

@Entity
@Table(name = "users")
@Getter
public class User extends AggregateRoot<UserId> {

    @Version
    private Long version;

    @Embedded
    private Username username;

    @Embedded
    private DisplayedName name;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    public User() {}

    private User(Username username, Password password, Email email, DisplayedName name) {
        super(UserId.randomId(UserId.class));
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static UserBuilder withUsername(String username) {
        return builder().username(username);
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Username username;
        private Password password;
        private Email email;
        private DisplayedName name;
        private Function<String, String> passwordEncoder = password -> password;

        private UserBuilder() {}

        public UserBuilder username(String username) {
            this.username = Username.from(username);
            return this;
        }

        public UserBuilder password(String password) {
            this.password = Password.from(password);
            return this;
        }

        public UserBuilder email(String email) {
            this.email = Email.from(email);
            return this;
        }

        public UserBuilder name(String name) {
            this.name = DisplayedName.from(name);
            return this;
        }

        public UserBuilder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "Encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        public User build() {
            String encodedPassword = this.passwordEncoder.apply(password.password());
            User user = new User(username, Password.from(encodedPassword), email, name);
            user.registerEvent(UserCreatedEvent.from(user));

            return user;
        }
    }
}
