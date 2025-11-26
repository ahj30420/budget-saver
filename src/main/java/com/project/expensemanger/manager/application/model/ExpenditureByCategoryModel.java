package com.project.expensemanger.manager.application.model;

public record ExpenditureByCategoryModel(
        Long categoryId,
        String categoryName,
        Long totalAmount
) {
}
