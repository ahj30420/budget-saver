package com.project.expensemanger.manager.application.service;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateBudgetRequest;
import com.project.expensemanger.manager.application.port.in.BudgetUseCase;
import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import com.project.expensemanger.manager.domain.budget.Budget;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
                    budgetPort.assertDateAndUserIdAndCategoryNotExists(budgetRequest.budgetDate(),
                            budgetRequest.categoryId(), userId);
                });
    }

    @Override
    public Budget getBudget(Long userId, Long budgetId) {
        return budgetPort.findByIdAndUserId(budgetId, userId);
    }

    @Override
    public List<Budget> getBudgetList(Long userId) {
        return budgetPort.findByUserId(userId);
    }

    @Override
    @Transactional
    public Long updateBudget(Long userId, Long budgetId, UpdateBudgetRequest requestDto) {
        Budget budget = getBudget(userId, budgetId);
        update(budget, requestDto);
        log.info("updateAmount: {}, updateCategoryId: {}", budget.getAmount(), budget.getCategoryId());
        budgetPort.update(budget);
        return budget.getId();
    }

    private void update(Budget budget, UpdateBudgetRequest request) {
        budget.updateAmount(request.amount());
        budget.updateCategory(request.categoryId());
    }
}
