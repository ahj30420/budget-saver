package com.project.expensemanger.manager.application.port.out;

import com.project.expensemanger.manager.domain.budget.Budget;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

public interface BudgetPort {
    List<Budget> saveAll(List<Budget> budgetList);

    void assertDateAndUserIdAndCategoryNotExists(LocalDate budgetDate, Long categoryId, Long userId);
}
