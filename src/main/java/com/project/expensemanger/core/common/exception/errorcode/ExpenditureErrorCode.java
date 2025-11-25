package com.project.expensemanger.core.common.exception.errorcode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ExpenditureErrorCode implements ErrorCode {
    EXPENDITURE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 예산이 존재하지 않습니다."),
    EXPENDITURE_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 지출에 대한 권한이 없습니다.");

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
