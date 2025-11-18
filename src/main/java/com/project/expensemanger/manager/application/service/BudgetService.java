package com.project.expensemanger.manager.application.service;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateBudgetRequest;
import com.project.expensemanger.manager.application.port.in.BudgetUseCase;
import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import com.project.expensemanger.manager.domain.budget.Budget;
import com.project.expensemanger.manager.domain.budget.recommendation.BudgetRecommendationContext;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.CategoryBudgetStat;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.RecommendedBudgetResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService implements BudgetUseCase {

    private final BudgetPort budgetPort;
    private final CategoryPort categoryPort;
    private final BudgetRecommendationContext recommendationContext;

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
    public Budget updateBudget(Long userId, Long budgetId, UpdateBudgetRequest requestDto) {
        Budget budget = getBudget(userId, budgetId);
        update(budget, requestDto);
        budgetPort.update(budget);
        return budget;
    }

    private void update(Budget budget, UpdateBudgetRequest request) {
        budget.updateAmount(request.amount());
        budget.updateCategory(request.categoryId());
    }

    @Override
    @Transactional
    public void deleteBudget(Long userId, Long budgetId) {
        Budget budget = getBudget(userId, budgetId);
        budgetPort.delete(budget);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecommendedBudgetResult> getRecommendBudgetByCategory(Long totalAmount) {
        List<CategoryBudgetStat> stats  = budgetPort.findTotalBudgetByCategory();
        return recommendationContext.recommend(totalAmount, stats);
    }
}
