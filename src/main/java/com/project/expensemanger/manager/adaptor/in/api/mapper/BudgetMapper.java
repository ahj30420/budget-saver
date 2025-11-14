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

    public BudgetResponse toBudgetDto(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .categoryId(budget.getCategoryId())
                .date(budget.getDate())
                .amount(budget.getAmount())
                .build();
    }
}
