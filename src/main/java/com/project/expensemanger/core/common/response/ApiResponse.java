package com.project.expensemanger.core.common.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
public class ApiResponse<T> {
    private String status;
    private T body;
    private LocalDateTime timestamp = LocalDateTime.now();

    @Builder
    public ApiResponse(String status, T body) {
        this.status = status;
        this.body = body;
    }
}
