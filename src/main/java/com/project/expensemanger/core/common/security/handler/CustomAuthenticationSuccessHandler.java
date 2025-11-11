package com.project.expensemanger.core.common.security.handler;

import com.project.expensemanger.core.common.security.jwt.JwtProperties;
import com.project.expensemanger.core.common.security.jwt.JwtProvider;
import com.project.expensemanger.core.common.security.vo.CustomUserDetails;
import com.project.expensemanger.core.common.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ResponseUtil responseUtil;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserIdx();
        String email = userDetails.getEmail();

        String accessToken = jwtProvider.generateAccessToken(email, userId, userDetails.getAuthorities());

        addTokensToResponse(response, accessToken);
        responseUtil.writeJsonSuccessResponse(response);
    }

    private void addTokensToResponse(HttpServletResponse response, String accessToken) {
        response.addHeader(HttpHeaders.AUTHORIZATION, jwtProperties.getTokenPrefix() + accessToken);
    }

}
