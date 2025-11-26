package com.project.expensemanger.manager.adaptor.in.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

public record GetExpenditureListRequest(
        Long categoryId,

        @NotNull
        @PastOrPresent
        LocalDateTime startDate,

        @NotNull
        @PastOrPresent
        LocalDateTime endDate,

        Long minAmount,

        Long maxAmount
) {
}
