package com.project.expensemanger.core.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.core.common.exception.errorcode.AuthErrorCode;
import com.project.expensemanger.core.common.response.ApiResponse;
import com.project.expensemanger.core.common.response.ErrorResponse;
import com.project.expensemanger.core.common.security.jwt.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseUtil {

    private final ObjectMapper objectMapper;
    private final CookieUtils cookieUtils;
    private final JwtProperties jwtProperties;

    public void writeJsonSuccessResponse(HttpServletResponse response) throws IOException {
        ApiResponse<Void> body = createSuccessBody();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try (OutputStream out = response.getOutputStream()) {
            objectMapper.writeValue(out, body);
        }
    }

    private ApiResponse<Void> createSuccessBody() {
        return ApiResponse.<Void>builder()
                .status("SUCCESS")
                .build();
    }

    public void writeJsonErrorResponse(HttpServletResponse response, AuthErrorCode errorCode)
            throws IOException {
        ApiResponse<ErrorResponse> body = createErrorBody(errorCode);
        response.setStatus(body.getBody().getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try (OutputStream out = response.getOutputStream()) {
            objectMapper.writeValue(out, body);
        }
    }

    private ApiResponse<ErrorResponse> createErrorBody(AuthErrorCode errorCode) {
        ErrorResponse errorBody = ErrorResponse.builder()
                .httpStatus(errorCode.getHttpStatus())
                .message(errorCode.getMessage())
                .build();

        return ApiResponse.<ErrorResponse>builder()
                .status("ERROR")
                .body(errorBody)
                .build();
    }

    public void addTokensToResponse(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie refreshTokenCookie = cookieUtils.createTokenCookie(refreshToken);
        response.addCookie(refreshTokenCookie);
        response.addHeader(HttpHeaders.AUTHORIZATION, jwtProperties.getTokenPrefix() + accessToken);
    }
}
