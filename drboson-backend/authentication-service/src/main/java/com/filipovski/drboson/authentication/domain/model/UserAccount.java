package com.filipovski.drboson.authentication.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.AggregateRoot;
import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

//@Getter
@Entity(name = "user_accounts")
public class UserAccount extends AggregateRoot<UserAccountId> implements UserDetails, CredentialsContainer {

    @Version
    private Long version;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Embedded
    private Username username;

    @Embedded
    private Password password;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    public UserAccount(UserId userId, Username username, Password password, boolean enabled,
                boolean accountNonExpired, boolean credentialsNonExpired,
                boolean accountNonLocked) {
        super(UserAccountId.randomId(UserAccountId.class));
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
    }

    protected UserAccount() { }

    public String getUserId() {
        return userId.getId();
    }

    @Override
    public void eraseCredentials() {
        password.erase();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public String getPassword() {
        return password.password();
    }

    @Override
    public String getUsername() {
        return username.username();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserAccount) {
            return userId.equals(((UserAccount) obj).userId);
        }

        return false;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    public static UserAccountBuilder builder() {
        return new UserAccountBuilder();
    }

    public static UserAccountBuilder username(String username) {
        return builder().username(username);
    }

    public static class UserAccountBuilder {
        private UserId userId;
        private Username username;
        private Password password;
        private boolean accountExpired;
        private boolean accountLocked;
        private boolean credentialsExpired;
        private boolean disabled;

        private UserAccountBuilder() {}

        public UserAccountBuilder userId(String id) {
            this.userId = new UserId(id);
            return this;
        }

        public UserAccountBuilder username(String username) {
            this.username = Username.from(username);
            return this;
        }

        public UserAccountBuilder password(String password) {
            this.password = Password.from(password);
            return this;
        }

        public UserAccountBuilder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public UserAccountBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public UserAccountBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public UserAccountBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public UserAccount build() {
            return new UserAccount(userId, username, password, !disabled, !accountExpired,
                    !credentialsExpired, !accountLocked);
        }
    }
}
