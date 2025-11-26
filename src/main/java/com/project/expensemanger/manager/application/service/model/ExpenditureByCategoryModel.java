package com.project.expensemanger.manager.application.service.model;

public record ExpenditureByCategoryModel(
        Long categoryId,
        String categoryName,
        Long totalAmount
) {
}
