package com.project.expensemanger.core.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.core.common.exception.errorcode.AuthErrorCode;
import com.project.expensemanger.core.common.response.ApiResponse;
import com.project.expensemanger.core.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseUtil {

    private final ObjectMapper objectMapper;

    public void writeJsonSuccessResponse(HttpServletResponse response) throws IOException {
        ApiResponse<Void> body = createSuccessBody();
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
}
