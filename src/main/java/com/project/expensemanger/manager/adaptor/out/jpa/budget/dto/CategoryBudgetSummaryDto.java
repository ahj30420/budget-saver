package com.project.expensemanger.manager.adaptor.out.jpa.budget.dto;

public record CategoryBudgetSummaryDto(
        Long categoryId,
        String categoryName,
        Long totalBudgetAmount
) {
}
