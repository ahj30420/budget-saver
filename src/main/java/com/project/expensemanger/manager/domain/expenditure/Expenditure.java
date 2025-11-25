package com.project.expensemanger.manager.domain.expenditure;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Expenditure {

    private Long id;
    private Long userId;
    private Long categoryId;

    private LocalDateTime spentAt;
    private Long amount;
    private String memo;

    private boolean excludedFromTotal;

    @Builder
    public Expenditure(Long id, Long userId, Long categoryId, LocalDateTime spentAt, Long amount, String memo,
                       boolean excludedFromTotal) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.spentAt = spentAt;
        this.amount = amount;
        this.memo = memo;
        this.excludedFromTotal = excludedFromTotal;
    }
}
