package com.project.expensemanger.manager.adaptor.out;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.UserErrorCode;
import com.project.expensemanger.manager.adaptor.out.jpa.user.UserJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.user.entity.UserJpaEntity;
import com.project.expensemanger.manager.application.port.out.UserPort;
import com.project.expensemanger.manager.domain.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findByEmail(String email) {
        return userJpaRepository.findByEmailAndIsDeletedFalse(email)
                .map(UserJpaEntity::toDomain)
                .orElseThrow(() -> new BaseException(UserErrorCode.NOT_EXIST_USER));
    }

    @Override
    public void assertEmailNotExists(String email) {
        userJpaRepository.findByEmailAndIsDeletedFalse(email)
                .ifPresent(user -> {
                    throw new BaseException(UserErrorCode.EMAIL_ALREADY_EXISTS);
                });
    }

    @Override
    public User save(User user) {
        UserJpaEntity saved = userJpaRepository.save(UserJpaEntity.from(user));
        return saved.toDomain();
    }
}
