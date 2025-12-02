package com.project.expensemanger.manager.application.mock;

import com.project.expensemanger.manager.domain.recommendation.CategoryBudgetUsage;
import org.springframework.stereotype.Component;

@Component
public class CategoryBudgetUsageMock {

    private final Long categoryId = 1L;
    private final String categoryName = "식비";

    private final Long defaultExpenditure = 20000L;
    private final Long defaultBudget = 50000L;


    private final Long overExpenditure = 25000L;
    private final Long lowBudget = 20000L;


    private final Long roundingExpenditure = 3333L;
    private final Long roundingBudget = 10000L;

    public CategoryBudgetUsage domainMock() {
        return CategoryBudgetUsage.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .expenditureAmount(defaultExpenditure)
                .budgetAmount(defaultBudget)
                .build();
    }

    public CategoryBudgetUsage zeroBudgetMock() {
        return CategoryBudgetUsage.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .expenditureAmount(overExpenditure)
                .budgetAmount(lowBudget)
                .build();
    }

    public CategoryBudgetUsage roundingMock() {
        return CategoryBudgetUsage.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .expenditureAmount(roundingExpenditure)
                .budgetAmount(roundingBudget)
                .build();
    }
}
