package com.project.expensemanger.manager.application.service.model;

import java.util.List;
import lombok.Builder;

@Builder
public record TodayBudgetRecommendationModel(
        long todayTotalPossible,
        List<CategoryRecommendtaionModel>categories,
        String message
) {
}
