package com.project.expensemanger.core.common.security.handler;

import com.project.expensemanger.core.common.security.jwt.JwtProvider;
import com.project.expensemanger.core.common.util.CookieUtils;
import com.project.expensemanger.core.common.util.ResponseUtil;
import com.project.expensemanger.manager.application.port.in.AuthUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ResponseUtil responseUtil;
    private final CookieUtils cookieUtils;
    private final JwtProvider jwtProvider;
    private final AuthUseCase authUseCase;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        invalidateRefreshToken(request, response, authentication);
        responseUtil.writeJsonSuccessResponse(response);
    }

    private void invalidateRefreshToken(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String refreshToken = cookieUtils.extractRefreshToken(request);

        if (!StringUtils.hasText(refreshToken)) {
            return;
        }

        Long userId = jwtProvider.getClaims(refreshToken).get("id", Long.class);

        authUseCase.delete(userId);
        response.addCookie(cookieUtils.deleteRefeshCookie());
    }
}
