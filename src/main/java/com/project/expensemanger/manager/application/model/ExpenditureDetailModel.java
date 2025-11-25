package com.project.expensemanger.manager.application.model;

import java.time.LocalDateTime;

public record ExpenditureDetailModel(
        Long expenditureId,
        Long userId,
        LocalDateTime spentAt,
        Long amount,
        String memo,
        Long categoryId,
        String categoryName
) {
}
