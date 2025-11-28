package com.project.expensemanger.manager.domain.expenditure;

import java.time.LocalDateTime;

public record ExpenditureUpdateCommand(
        Long amount,
        LocalDateTime spentAt,
        String memo,
        Long categoryId,
        boolean excludedFromTotal
) {}
