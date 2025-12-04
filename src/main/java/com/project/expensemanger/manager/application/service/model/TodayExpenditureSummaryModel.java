package com.project.expensemanger.manager.application.service.model;

import java.util.List;

public record TodayExpenditureSummaryModel(
        long totalTodayAmount,
        List<TodayCategoryExpenditureModel> categories
) {}