package com.project.expensemanger.manager.application.port.out;

import com.project.expensemanger.manager.domain.budget.Budget;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.CategoryBudgetStat;
import java.time.LocalDate;
import java.util.List;

public interface BudgetPort {
    List<Budget> saveAll(List<Budget> budgetList);

    void assertDateAndUserIdAndCategoryNotExists(LocalDate budgetDate, Long categoryId, Long userId);

    Budget findByIdAndUserId(Long budgetId, Long userId);

    List<Budget> findByUserId(Long userId);

    void update(Budget updateBudget);

    void delete(Budget budget);

    List<CategoryBudgetStat> findTotalBudgetByCategory();
}
