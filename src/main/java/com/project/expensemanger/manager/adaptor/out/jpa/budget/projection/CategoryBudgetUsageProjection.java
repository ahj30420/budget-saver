package com.project.expensemanger.manager.adaptor.out.jpa.budget.projection;

public record CategoryBudgetUsageProjection(
        Long categoryId,
        String categoryName,
        Long expenditureAmount,
        Long budgetAmount
) {
}
