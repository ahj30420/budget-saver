package com.project.expensemanger.manager.domain.budget.recommendation.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendedBudgetResult {
    private Long categoryId;
    private String categoryName;
    private Long recommendedAmount;

    public RecommendedBudgetResult(Long categoryId, String categoryName, Long recommendedAmount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.recommendedAmount = recommendedAmount;
    }
}