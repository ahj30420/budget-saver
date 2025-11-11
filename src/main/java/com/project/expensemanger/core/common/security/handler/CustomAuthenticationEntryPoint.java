package com.project.expensemanger.core.common.security.handler;

import com.project.expensemanger.core.common.exception.errorcode.AuthErrorCode;
import com.project.expensemanger.core.common.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ResponseUtil responseUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        responseUtil.writeJsonErrorResponse(response, AuthErrorCode.UNAUTHENTICATED);
    }
}