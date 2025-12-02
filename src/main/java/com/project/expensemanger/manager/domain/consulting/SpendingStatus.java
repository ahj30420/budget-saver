package com.project.expensemanger.manager.domain.consulting;

import lombok.Getter;

@Getter
public enum SpendingStatus {

    GOOD("잘 아끼고 있어요!"),
    NORMAL("계획대로 소비 중이에요."),
    WARN("소비 속도가 조금 빠른 편이에요."),
    OVER("예산을 초과했어요!");

    private final String message;

    SpendingStatus(String message) {
        this.message = message;
    }

    public static SpendingStatus fromRate(double rate) {
        if (rate < 80) {
            return GOOD;
        }
        if (rate < 100) {
            return NORMAL;
        }
        if (rate < 120) {
            return WARN;
        }
        return OVER;
    }
}
