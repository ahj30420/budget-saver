package com.project.expensemanger.manager.application.service.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record GetBudgetUsageCondition(
        LocalDate startDate,
        LocalDate endDate,
        Long userId,
        List<Long> categoryIdList
) {
}
