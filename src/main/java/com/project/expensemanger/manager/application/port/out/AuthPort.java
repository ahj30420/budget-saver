package com.project.expensemanger.manager.application.port.out;

public interface AuthPort {
    void saveRefreshToken(Long userId, String refreshToken);

    void deleteRefreshToken(Long userId);
}
