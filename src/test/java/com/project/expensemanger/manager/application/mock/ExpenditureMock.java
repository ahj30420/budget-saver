package com.project.expensemanger.manager.application.mock;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateExpenditureRequest;
import com.project.expensemanger.manager.application.service.model.ExpenditureDetailModel;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import com.project.expensemanger.manager.domain.expenditure.ExpenditureUpdateCommand;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class ExpenditureMock {

    private final Long id = 1L;

    private final LocalDateTime spentAt = LocalDateTime.of(2026, 11, 25, 12, 30, 10);
    private final LocalDateTime changedSpendAt = LocalDateTime.of(2026, 10, 25, 12, 30, 10);

    private final Long amount = 1000L;
    private final Long changedAmount = 2000L;
    private final Long zeroAmount = 0L;
    private final Long negativeAmount = -1L;

    private final String memo = "메모 테스트";
    private final String changedMemo = "메모 수정";

    private final boolean excludedFromTotal = false;
    private final boolean changedExcludedFromTotal = true;

    private final Long categoryId = 1L;
    private final Long changedCategoryId = 2L;
    private final Long zeroCategoryId = 0L;
    private final Long negativeCategoryId = -1L;

    private final Long userId = 1L;
    private final Long otherUserId = 2L;

    public Expenditure domainMock() {
        return Expenditure.builder()
                .id(id)
                .spentAt(spentAt)
                .amount(amount)
                .memo(memo)
                .excludedFromTotal(excludedFromTotal)
                .categoryId(categoryId)
                .userId(userId)
                .build();
    }

    public RegisterExpenditure RegisterRequestDto() {
        return new RegisterExpenditure(
                categoryId,
                amount,
                spentAt,
                memo
        );
    }

    public UpdateExpenditureRequest updateRequestDto() {
        return new UpdateExpenditureRequest(
                changedAmount,
                changedSpendAt,
                changedMemo,
                changedExcludedFromTotal,
                changedCategoryId
        );
    }

    public ExpenditureUpdateCommand updateRequestCommand() {
        return new ExpenditureUpdateCommand(
                changedAmount,
                changedSpendAt,
                changedMemo,
                changedCategoryId,
                changedExcludedFromTotal
        );
    }

    public ExpenditureUpdateCommand zeroAmountCommand() {
        return new ExpenditureUpdateCommand(
                zeroAmount,
                changedSpendAt,
                changedMemo,
                changedCategoryId,
                changedExcludedFromTotal
        );
    }

    public ExpenditureUpdateCommand negativeAmountCommand() {
        return new ExpenditureUpdateCommand(
                negativeAmount,
                changedSpendAt,
                changedMemo,
                changedCategoryId,
                changedExcludedFromTotal
        );
    }

    public ExpenditureUpdateCommand nullAmountCommand() {
        return new ExpenditureUpdateCommand(
                null,
                changedSpendAt,
                changedMemo,
                changedCategoryId,
                changedExcludedFromTotal
        );
    }

    public ExpenditureUpdateCommand zeroCategoryIdCommand() {
        return new ExpenditureUpdateCommand(
                changedAmount,
                changedSpendAt,
                changedMemo,
                zeroCategoryId,
                changedExcludedFromTotal
        );
    }

    public ExpenditureUpdateCommand negativeCategoryIdCommand() {
        return new ExpenditureUpdateCommand(
                changedAmount,
                changedSpendAt,
                changedMemo,
                negativeCategoryId,
                changedExcludedFromTotal
        );
    }

    public ExpenditureUpdateCommand nullCategoryIdCommand() {
        return new ExpenditureUpdateCommand(
                changedAmount,
                changedSpendAt,
                changedMemo,
                null,
                changedExcludedFromTotal
        );
    }

    public ExpenditureUpdateCommand nullSpendAtCommand() {
        return new ExpenditureUpdateCommand(
                changedAmount,
                null,
                changedMemo,
                changedCategoryId,
                changedExcludedFromTotal
        );
    }

    public ExpenditureDetailModel expenditureDetailModel() {
        return new ExpenditureDetailModel(
                id,
                userId,
                spentAt,
                amount,
                memo,
                categoryId,
                "카테고리 이름"
        );
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOtherUserId() {
        return otherUserId;
    }
}
