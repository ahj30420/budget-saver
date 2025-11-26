package com.project.expensemanger.manager.domain.expenditure;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.ExpenditureErrorCode;
import com.project.expensemanger.manager.application.mock.ExpenditureMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExpenditureTest {

    ExpenditureMock expenditureMock = new ExpenditureMock();
    Expenditure expenditure;

    @BeforeEach
    void setUp() {
        expenditure = expenditureMock.domainMock();
    }

    @Test
    @DisplayName("지출 수정 테스트 : 성공")
    void update_expenditure_success_test() throws Exception {
        // given
        ExpenditureUpdateCommand command = expenditureMock.updateRequestCommand();

        // when
        expenditure.update(command);

        // then
        assertEquals(expenditure.getAmount(), command.amount());
        assertEquals(expenditure.getSpentAt(), command.spentAt());
        assertEquals(expenditure.getMemo(), command.memo());
        assertEquals(expenditure.isExcludedFromTotal(), command.excludedFromTotal());
        assertEquals(expenditure.getCategoryId(), command.categoryId());
    }

    @Test
    @DisplayName("지출 금액 수정 테스트 : 실패[수정된 금액이 null 일 경우]")
    void update_amount_fail_when_changed_amount_is_null() throws Exception {
        // given
        ExpenditureUpdateCommand nullAmountCommand = expenditureMock.nullAmountCommand();

        // when & then
        assertThatThrownBy(() -> expenditure.update(nullAmountCommand))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.INVALID_EXPENDITURE_AMOUNT.getMessage());
    }

    @Test
    @DisplayName("지출 금액 수정 테스트 : 실패[수정된 금액이 0 일 경우]")
    void update_amount_fail_when_changed_amount_is_zero() throws Exception {
        // given
        ExpenditureUpdateCommand zeroAmountCommand = expenditureMock.zeroAmountCommand();

        // when & then
        assertThatThrownBy(() -> expenditure.update(zeroAmountCommand))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.INVALID_EXPENDITURE_AMOUNT.getMessage());
    }

    @Test
    @DisplayName("지출 금액 수정 테스트 : 실패[수정된 금액이 음수 일 경우]")
    void update_amount_fail_when_changed_amount_is_negative() throws Exception {
        // given
        ExpenditureUpdateCommand negativeAmountCommand = expenditureMock.negativeAmountCommand();

        // when & then
        assertThatThrownBy(() -> expenditure.update(negativeAmountCommand))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.INVALID_EXPENDITURE_AMOUNT.getMessage());
    }

    @Test
    @DisplayName("지출 카테고리 수정 테스트 : 실패[카테고리 id가 null 일 경우]")
    void update_category_fail_when_category_id_is_null() throws Exception {
        // given
        ExpenditureUpdateCommand nullCategoryIdCommand = expenditureMock.nullCategoryIdCommand();

        // when & then
        assertThatThrownBy(() -> expenditure.update(nullCategoryIdCommand))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.INVALID_EXPENDITURE_CATEGORY.getMessage());
    }

    @Test
    @DisplayName("지출 카테고리 수정 테스트 : 실패[카테고리 id가 0일 경우]")
    void update_category_fail_when_category_id_is_zero() throws Exception {
        // given
        ExpenditureUpdateCommand zeroCategoryIdCommand = expenditureMock.zeroCategoryIdCommand();

        // when & then
        assertThatThrownBy(() -> expenditure.update(zeroCategoryIdCommand))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.INVALID_EXPENDITURE_CATEGORY.getMessage());
    }

    @Test
    @DisplayName("지출 카테고리 수정 테스트 : 실패[카테고리 id가 음수 일 경우]")
    void update_category_fail_when_category_id_is_negative() throws Exception {
        // given
        ExpenditureUpdateCommand negativeCategoryIdCommand = expenditureMock.negativeCategoryIdCommand();

        // when & then
        assertThatThrownBy(() -> expenditure.update(negativeCategoryIdCommand))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.INVALID_EXPENDITURE_CATEGORY.getMessage());
    }

    @Test
    @DisplayName("지출 일시 수정 테스트 : 실패[지출 일시가 null 일 경우]")
    void update_spendAt_fail_when_spendAt_is_null() throws Exception {
        // given
        ExpenditureUpdateCommand nullSpendAtCommand = expenditureMock.nullSpendAtCommand();

        // when & then
        assertThatThrownBy(() -> expenditure.update(nullSpendAtCommand))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.INVALID_EXPENDITURE_SPENTAT.getMessage());
    }

}