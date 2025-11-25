package com.project.expensemanger.manager.adaptor.out.jpa.budget.projection;

public interface CategoryBudgetSummaryProjection {
    Long getCategoryId();
    String getCategoryName();
    Long getTotalBudgetAmount();
}
