package com.project.expensemanger.manager.domain.budget;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Budget {
    private Long id;
    private Long userId;
    private Long categoryId;
    private LocalDate date;
    private Long amount;

    @Builder
    public Budget(Long id, Long userId, Long categoryId, LocalDate date, Long amount) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.date = date;
        this.amount = amount;
    }
}
