package com.project.expensemanger.manager.application.service.notification.event;

import com.project.expensemanger.manager.application.service.model.TodayBudgetRecommendationModel;

public record DailyRecommendationEvent(
        TodayBudgetRecommendationModel recommendation,
        Long userId
) {
}
