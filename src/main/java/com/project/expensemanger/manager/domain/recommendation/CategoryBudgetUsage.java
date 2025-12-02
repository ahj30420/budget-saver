package com.project.expensemanger.manager.domain.recommendation;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryBudgetUsage {
    private Long categoryId;
    private String categoryName;
    private Long expenditureAmount;
    private Long budgetAmount;
    private final long minDailyExpenditure = 5000;

    @Builder
    public CategoryBudgetUsage(Long categoryId, String categoryName, Long expenditureAmount, Long budgetAmount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.expenditureAmount = expenditureAmount;
        this.budgetAmount = budgetAmount;
    }

    public long getRemainingBudgetAmount() {
        return budgetAmount - expenditureAmount;
    }

    public long getPossibleTodayExpenditure(long remainingDays) {
        long daliyExpenditure = getRemainingBudgetAmount() / remainingDays;
        if (daliyExpenditure <= 0) {
            return minDailyExpenditure;
        }
        return Math.round(daliyExpenditure / 100.0) * 100;
    }
}
