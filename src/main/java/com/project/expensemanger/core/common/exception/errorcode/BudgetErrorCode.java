package com.project.expensemanger.core.common.exception.errorcode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BudgetErrorCode implements ErrorCode {
    BUDGET_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 예산이 존재합니다."),
    BUDGET_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 예산이 존재하지 않습니다."),
    INVALID_BUDGET_AMOUNT(HttpStatus.BAD_REQUEST, "예산 금액이 올바르지 않습니다."),
    INVALID_BUDGET_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리가 올바르지 않습니다."),
    NOT_FOUND_RECOMMEND_STRATEGY(HttpStatus.NOT_FOUND, "해당 추천 전략을 찾을 수 없습니다.");

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
