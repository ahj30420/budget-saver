package com.project.expensemanger.core.common.exception.advice;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.CommonErrorCode;
import com.project.expensemanger.core.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<Object> BaseException(final BaseException e) {
        log.error("[BaseException] {} - {}", e.getHttpStatus().value(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(new ErrorResponse(e.getHttpStatus(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(final Exception e) {
        HttpStatus errroStatus = CommonErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus();
        log.error("[{}] {}", e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(errroStatus)
                .body(new ErrorResponse(errroStatus, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleValidationException(final MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("[MethodArgumentNotValidException] {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage));
    }

}

