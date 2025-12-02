package com.project.expensemanger.manager.adaptor.out.jpa.budget.projection;

public interface CategoryBudgetUsageProjection {
    Long getCategoryId();
    String getCategoryName();
    Long getExpenditureAmount();
    Long getBudgetAmount();
}
