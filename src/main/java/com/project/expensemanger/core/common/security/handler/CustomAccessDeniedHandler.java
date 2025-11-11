package com.project.expensemanger.core.common.security.handler;

import com.project.expensemanger.core.common.exception.errorcode.AuthErrorCode;
import com.project.expensemanger.core.common.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ResponseUtil responseUtil;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        responseUtil.writeJsonErrorResponse(response, AuthErrorCode.UNAUTHENTICATED);
    }
}