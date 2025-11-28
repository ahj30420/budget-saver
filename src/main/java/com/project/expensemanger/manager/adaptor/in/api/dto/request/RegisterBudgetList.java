package com.project.expensemanger.manager.adaptor.in.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record RegisterBudgetList(
        @NotEmpty(message = "적어도 하나 이상의 예산을 등록해야 합니다.")
        List<RegisterBudget> budgets
) {
}
