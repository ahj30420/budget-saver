package com.project.expensemanger.manager.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private UserRole role;

    @Builder
    public User(Long id, String email, String password, String name, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
