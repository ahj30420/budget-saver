package com.project.expensemanger.manager.adaptor.out.jpa.user;

import com.project.expensemanger.manager.adaptor.out.jpa.user.entity.UserJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByEmailAndIsDeletedFalse(String email);
}
