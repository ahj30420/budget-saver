package com.project.expensemanger.manager.application.port.out;

import com.project.expensemanger.manager.domain.User.User;

public interface UserPort {
    void assertEmailNotExists(String email);

    void save(User user);
}
