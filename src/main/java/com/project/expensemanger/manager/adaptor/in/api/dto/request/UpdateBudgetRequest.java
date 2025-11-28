package com.project.expensemanger.manager.adaptor.in.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateBudgetRequest(
        @Positive
        Long amount,

        @NotNull
        Long categoryId
) {
}
