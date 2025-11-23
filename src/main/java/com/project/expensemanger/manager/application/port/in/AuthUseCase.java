package com.project.expensemanger.manager.application.port.in;

public interface AuthUseCase {
    void registerRefreshToken(Long userId, String refreshToken);
}
