package com.project.expensemanger.manager.adaptor.out.jpa.budget.projection;

public record CategoryBudgetSummaryDto(
        Long categoryId,
        String categoryName,
        Long totalBudgetAmount
) {
}
