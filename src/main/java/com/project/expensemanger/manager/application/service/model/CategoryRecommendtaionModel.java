package com.project.expensemanger.manager.application.service.model;

import lombok.Builder;

@Builder
public record CategoryRecommendtaionModel(
        Long categoryId,
        String categoryName,
        long recommendedAmount
) {
}
