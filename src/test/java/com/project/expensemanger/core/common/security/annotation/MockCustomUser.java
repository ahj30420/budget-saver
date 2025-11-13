package com.project.expensemanger.core.common.security.annotation;

import com.project.expensemanger.manager.domain.User.UserRole;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockCustomUserSecurityContextFactory.class)
public @interface MockCustomUser {
    long userId() default 1L;

    UserRole userRole() default UserRole.USER;
}
