package com.project.expensemanger.manager.application.mock;

import com.project.expensemanger.manager.domain.User.User;
import com.project.expensemanger.manager.domain.User.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMock {

    private final PasswordEncoder encoder;
    private final Long id = 1L;
    private final String email = "test@naver.com";
    private final String password = "testpw1234";
    private final String name = "test";
    private final UserRole userRole = UserRole.USER;

    public UserMock(PasswordEncoder passwordEncoder) { this.encoder = passwordEncoder; }

    public User domainMock() {
        return User.builder()
                .id(id)
                .email(email)
                .password(encoder.encode(password))
                .name(name)
                .role(userRole)
                .build();
    }

    public String getBeforeEncodedPw() {
        return this.password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
