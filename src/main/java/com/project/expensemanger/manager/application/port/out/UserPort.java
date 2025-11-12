package com.project.expensemanger.manager.application.port.out;

import com.project.expensemanger.manager.domain.User.User;

public interface UserPort {
    User findByEmail(String email);

    void assertEmailNotExists(String email);

    User save(User user);
}
