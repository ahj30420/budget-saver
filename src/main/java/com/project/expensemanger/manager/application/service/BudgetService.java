package com.project.expensemanger.manager.application.service;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.application.port.in.BudgetUseCase;
import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import com.project.expensemanger.manager.domain.budget.Budget;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService implements BudgetUseCase {

    private final BudgetPort budgetPort;
    private final CategoryPort categoryPort;

    @Override
    public List<Long> registerBudget(Long userId, RegisterBudgetList requestDto) {
        validateDuplicateBudgets(userId, requestDto);
        List<Budget> budgetList = requestDto.budgets().stream()
                .map((b) -> {
                    return Budget.builder()
                            .userId(userId)
                            .categoryId(b.categoryId())
                            .date(b.budgetDate())
                            .amount(b.amount())
                            .build();
                }).toList();

        List<Budget> budgets = budgetPort.saveAll(budgetList);

        return budgets.stream().map(Budget::getId).toList();
    }

    private void validateDuplicateBudgets(Long userId, RegisterBudgetList requestDto) {
        requestDto.budgets().stream()
                .forEach(budgetRequest -> {
                    categoryPort.findById(budgetRequest.categoryId());
                    budgetPort.assertDateAndUserIdAndCategoryNotExists(budgetRequest.budgetDate(), budgetRequest.categoryId(), userId);
                });
    }
}
