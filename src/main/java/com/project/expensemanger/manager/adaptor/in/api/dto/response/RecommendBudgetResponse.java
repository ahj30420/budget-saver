package com.project.expensemanger.manager.adaptor.in.api.dto.response;

import lombok.Builder;

@Builder
public record RecommendBudgetResponse(
        Long categoryId,
        String categoryName,
        Long recommendedAmount
) {
}
