package com.project.expensemanger.core.common.security.handler;

import com.project.expensemanger.core.common.security.jwt.JwtProperties;
import com.project.expensemanger.core.common.security.jwt.JwtProvider;
import com.project.expensemanger.core.common.security.vo.CustomUserDetails;
import com.project.expensemanger.core.common.util.ResponseUtil;
import com.project.expensemanger.manager.application.port.in.AuthUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ResponseUtil responseUtil;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final AuthUseCase authUseCase;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserIdx();
        String email = userDetails.getEmail();

        String accessToken = jwtProvider.generateAccessToken(email, userId, userDetails.getAuthorities());
        String refreshToken = jwtProvider.generateRefreshToken(email, userId);

        authUseCase.registerRefreshToken(userId, refreshToken);

        responseUtil.addTokensToResponse(response, accessToken, refreshToken);
        responseUtil.writeJsonSuccessResponse(response);
    }
}
