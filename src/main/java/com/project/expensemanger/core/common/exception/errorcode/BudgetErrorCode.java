package com.project.expensemanger.core.common.exception.errorcode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BudgetErrorCode implements ErrorCode {
    BUDGET_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 예산이 존재합니다."),
    BUDGET_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 예산이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
