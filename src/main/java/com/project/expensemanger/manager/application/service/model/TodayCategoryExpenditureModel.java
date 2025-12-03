package com.project.expensemanger.manager.application.service.model;

public record TodayCategoryExpenditureModel(
        Long categoryId,
        String categoryName,
        long todayAmount,
        long todayRecommendedAmount,
        long riskPercentage
) {}