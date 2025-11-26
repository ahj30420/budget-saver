package com.project.expensemanger.manager.adaptor.out.jpa.expenditure.projection;

public record ExpenditureByCategoryProjection(
        Long categoryId,
        String categoryName,
        Long totalAmount
) {
}
