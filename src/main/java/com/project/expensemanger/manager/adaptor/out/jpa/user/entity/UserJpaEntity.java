package com.project.expensemanger.manager.adaptor.out.jpa.user.entity;

import com.project.expensemanger.core.common.audting.BaseDateTime;
import com.project.expensemanger.manager.domain.User.User;
import com.project.expensemanger.manager.domain.User.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJpaEntity extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRoleType role;

    private boolean isDeleted = false;

    @Builder
    public UserJpaEntity(String email, String password, String name, UserRoleType role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public static UserJpaEntity from(User user) {
        return UserJpaEntity.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .role(UserRoleType.valueOf(user.getRole().name()))
                .build();
    }

    public User toDomain() {
        return User.builder()
                .id(this.getId())
                .email(this.getEmail())
                .password(this.getPassword())
                .name(this.getName())
                .role(UserRole.valueOf(this.getRole().name()))
                .build();
    }
}
