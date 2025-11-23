package com.project.expensemanger.manager.application.service;

import com.project.expensemanger.manager.application.port.in.AuthUseCase;
import com.project.expensemanger.manager.application.port.out.AuthPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AuthPort authPort;

    @Override
    public void registerRefreshToken(Long userId, String refreshToken) {
        authPort.saveRefreshToken(userId, refreshToken);
    }

    @Override
    public void delete(Long userId) {
        authPort.deleteRefreshToken(userId);
    }
}
