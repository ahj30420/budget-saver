package com.project.expensemanger.manager.domain.expenditure;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.ExpenditureErrorCode;
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

    public void update(ExpenditureUpdateCommand cmd) {
        updateAmount(cmd.amount());
        updateSpentAt(cmd.spentAt());
        updateMemo(cmd.memo());
        updateCategory(cmd.categoryId());
        updateExcludedFromTotal(cmd.excludedFromTotal());
    }

    private void updateAmount(Long changedAmount) {
        if (changedAmount == null || changedAmount <= 0) {
            throw new BaseException(ExpenditureErrorCode.INVALID_EXPENDITURE_AMOUNT);
        }
        this.amount = changedAmount;
    }

    private void updateCategory(Long changedCategoryId) {
        if (changedCategoryId == null || changedCategoryId <= 0) {
            throw new BaseException(ExpenditureErrorCode.INVALID_EXPENDITURE_CATEGORY);
        }
        this.categoryId = changedCategoryId;
    }

    private void updateSpentAt(LocalDateTime changedSpendAt) {
        if (changedSpendAt == null) {
            throw new BaseException(ExpenditureErrorCode.INVALID_EXPENDITURE_SPENTAT);
        }
        this.spentAt = changedSpendAt;
    }

    private void updateMemo(String changedMemo) {
        this.memo = changedMemo;
    }

    private void updateExcludedFromTotal(boolean changedExcludedFromTotal) {
        this.excludedFromTotal = changedExcludedFromTotal;
    }
}
