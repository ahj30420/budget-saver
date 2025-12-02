package com.project.expensemanger.manager.application.service.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryRecommendtaionModel {
    private Long categoryId;
    private String categoryName;
    private long recommendedAmount;

    @Builder
    public CategoryRecommendtaionModel(Long categoryId, String categoryName, long recommendedAmount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.recommendedAmount = recommendedAmount;
    }
}
