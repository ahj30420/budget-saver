package com.project.expensemanger.manager.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthUseCase {
    void registerRefreshToken(Long userId, String refreshToken);

    void delete(Long userId);

    void reissue(HttpServletRequest request, HttpServletResponse response);
}
