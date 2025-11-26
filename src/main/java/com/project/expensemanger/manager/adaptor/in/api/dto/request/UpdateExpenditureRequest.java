package com.project.expensemanger.manager.adaptor.in.api.dto.request;

import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record UpdateExpenditureRequest(
        @Positive
        Long amount,

        LocalDateTime spentAt,

        String memo,

        boolean excludedFromTotal,

        @Positive
        Long categoryId
) {
}
