package com.project.expensemanger.manager.domain.budget;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.BudgetErrorCode;
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

    public void updateAmount(Long changedAmount) {
        if (changedAmount == null || changedAmount <= 0) {
            throw new BaseException(BudgetErrorCode.INVALID_BUDGET_AMOUNT);
        }
        this.amount = changedAmount;
    }

    public void updateCategory(Long changedCategoryId) {
        if (changedCategoryId == null || changedCategoryId <= 0) {
            throw new BaseException(BudgetErrorCode.INVALID_BUDGET_CATEGORY);
        }
        this.categoryId = changedCategoryId;
    }
}
