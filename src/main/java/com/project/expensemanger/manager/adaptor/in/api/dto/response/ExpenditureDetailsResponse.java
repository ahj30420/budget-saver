package com.project.expensemanger.manager.adaptor.in.api.dto.response;

import java.time.LocalDateTime;

public record ExpenditureDetailsResponse(
        Long expenditureId,
        LocalDateTime spentAt,
        Long amount,
        String memo,
        GetCategoryResponse category
) {
}
