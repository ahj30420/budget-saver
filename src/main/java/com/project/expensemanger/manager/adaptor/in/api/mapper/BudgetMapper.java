package com.project.expensemanger.manager.adaptor.in.api.mapper;

import com.project.expensemanger.manager.adaptor.in.api.dto.response.BudgeIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.BudgetResponse;
import com.project.expensemanger.manager.domain.budget.Budget;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {

    public List<BudgeIdResponse> toIdListDto(List<Long> budgetIdList) {
        return budgetIdList.stream().map(BudgeIdResponse::new).toList();
    }

    public BudgeIdResponse toIdDto(Long budgetId) {
        return new BudgeIdResponse(budgetId);
    }

    public BudgetResponse toBudgetDto(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .categoryId(budget.getCategoryId())
                .date(budget.getDate())
                .amount(budget.getAmount())
                .build();
    }

    public List<BudgetResponse> toBudgetListDto(List<Budget> budgets) {
        return budgets.stream()
                .map(b -> {
                            return BudgetResponse.builder()
                                    .id(b.getId())
                                    .categoryId(b.getCategoryId())
                                    .date(b.getDate())
                                    .amount(b.getAmount())
                                    .build();
                        }).toList();
    }
}
