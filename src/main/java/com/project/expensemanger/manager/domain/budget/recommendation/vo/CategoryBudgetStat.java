package com.project.expensemanger.manager.domain.budget.recommendation.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryBudgetStat {
    private final Long categoryId;
    private final String categoryName;
    private final Long amount;

    @Builder
    public CategoryBudgetStat(Long categoryId, String categoryName, Long amount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amount = amount;
    }
}
