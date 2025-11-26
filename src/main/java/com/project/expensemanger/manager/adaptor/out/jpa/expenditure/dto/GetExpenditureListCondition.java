package com.project.expensemanger.manager.adaptor.out.jpa.expenditure.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GetExpenditureListCondition(
        Long categoryId,
        Long userId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long minAmount,
        Long maxAmount
) {
}
