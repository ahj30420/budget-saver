package com.project.expensemanger.manager.adaptor.in.api.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record BudgetResponse(
        Long id,
        Long categoryId,
        LocalDate date,
        Long amount
) {
}
