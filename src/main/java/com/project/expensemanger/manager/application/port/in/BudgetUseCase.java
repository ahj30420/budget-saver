package com.project.expensemanger.manager.application.port.in;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateBudgetRequest;
import com.project.expensemanger.manager.domain.budget.Budget;
import jakarta.validation.Valid;
import java.util.List;

public interface BudgetUseCase {
    List<Long> registerBudget(Long userId, RegisterBudgetList requestDto);

    Budget getBudget(Long userId, Long budgetId);

    List<Budget> getBudgetList(Long userId);

    Budget updateBudget(Long userId, Long budgetId, UpdateBudgetRequest requestDto);
}
