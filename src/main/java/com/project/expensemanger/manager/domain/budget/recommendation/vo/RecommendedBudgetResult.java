package com.project.expensemanger.manager.domain.budget.recommendation.vo;

import lombok.Getter;

@Getter
public class RecommendedBudgetResult {
    private final Long categoryId;
    private final String categoryName;
    private final Long recommendedAmount;

    public RecommendedBudgetResult(Long categoryId, String categoryName, Long recommendedAmount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.recommendedAmount = recommendedAmount;
    }
}