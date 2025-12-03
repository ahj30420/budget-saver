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
    private boolean notificationSubscribed;

    @Builder
    public User(Long id, String email, String password, String name, UserRole role, boolean notificationSubscribed) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.notificationSubscribed = notificationSubscribed;
    }
}
