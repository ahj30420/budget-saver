package com.project.expensemanger.manager.adaptor.in.api.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record RegisterExpenditure(
        @NotNull
        @Positive
        Long categoryId,

        @NotNull
        @Positive
        Long amount,

        @NotNull
        @FutureOrPresent
        LocalDateTime spentAt,

        String memo
) {
}
