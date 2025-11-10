package com.project.expensemanger.core.common.exception;

import com.project.expensemanger.core.common.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() { return errorCode.getHttpStatus(); }

    public String getErrorMessage() { return errorCode.getMessage(); }
}
