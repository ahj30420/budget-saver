package com.project.expensemanger.manager.adaptor.in.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ExpenditureListResponse(
        List<ExpenditureInfo> expenditures,
        Long totalAmount,
        List<CategoryAmount> categoryAmounts
) {

    @Builder
    public record ExpenditureInfo(
            Long expenditureId,
            Long categoryId,
            LocalDateTime spendAt,
            Long amount
    ) {}

    @Builder
    public record CategoryAmount(
            Long categoryId,
            String categoryName,
            Long totalAmount
    ) {}
}
