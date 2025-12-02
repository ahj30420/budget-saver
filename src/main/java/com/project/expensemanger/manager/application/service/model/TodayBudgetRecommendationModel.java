package com.project.expensemanger.manager.application.service.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TodayBudgetRecommendationModel {
    private long todayTotalPossible;
    private List<CategoryRecommendtaionModel> categories;
    private String message;

    @Builder
    public TodayBudgetRecommendationModel(long todayTotalPossible, List<CategoryRecommendtaionModel> categories, String message) {
        this.todayTotalPossible = todayTotalPossible;
        this.categories = categories;
        this.message = message;
    }
}
