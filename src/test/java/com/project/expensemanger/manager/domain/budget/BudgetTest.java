package com.project.expensemanger.manager.domain.budget;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.BudgetErrorCode;
import com.project.expensemanger.manager.application.mock.BudgetMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BudgetTest {

    BudgetMock budgetMock = new BudgetMock();
    Budget budget;

    @BeforeEach
    void setUp() {
        budget = budgetMock.domainMock();
    }

    @Test
    @DisplayName("예산 금액 수정 테스트 : 성공")
    void update_amount_success_test() throws Exception {
        // given
        Long changedAmount = 1000L;

        // when
        budget.updateAmount(changedAmount);

        // then
        assertEquals(changedAmount, budget.getAmount());
    }

    @Test
    @DisplayName("예산 금액 수정 테스트 : 실패[수정된 금액이 null 일 경우]")
    void update_amount_fail_when_changed_amount_is_null() throws Exception {
        // given
        Long changedAmount = null;

        // when & then
        assertThatThrownBy(() -> budget.updateAmount(changedAmount))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.INVALID_BUDGET_AMOUNT.getMessage());
    }

    @Test
    @DisplayName("예산 금액 수정 테스트 : 실패[수정된 금액이 0 일 경우]")
    void update_amount_fail_when_changed_amount_is_zero() throws Exception {
        // given
        Long changedAmount = 0L;

        // when & then
        assertThatThrownBy(() -> budget.updateAmount(changedAmount))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.INVALID_BUDGET_AMOUNT.getMessage());
    }

    @Test
    @DisplayName("예산 금액 수정 테스트 : 실패[수정된 금액이 음수 일 경우]")
    void update_amount_fail_when_changed_amount_is_negative() throws Exception {
        // given
        Long changedAmount = -1L;

        // when & then
        assertThatThrownBy(() -> budget.updateAmount(changedAmount))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.INVALID_BUDGET_AMOUNT.getMessage());
    }

    @Test
    @DisplayName("예산 카테고리 수정 테스트 : 성공")
    void update_category_success_test() throws Exception {
        // given
        Long changedCategoryId = 2L;

        // when
        budget.updateCategory(changedCategoryId);

        // then
        assertEquals(changedCategoryId, budget.getCategoryId());
    }

    @Test
    @DisplayName("예산 카테고리 수정 테스트 : 실패[카테고리 id가 null 일 경우]")
    void update_category_fail_when_category_id_is_null() throws Exception {
        // given
        Long categoryId = null;

        // when & then
        assertThatThrownBy(() -> budget.updateCategory(categoryId))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.INVALID_BUDGET_CATEGORY.getMessage());
    }

    @Test
    @DisplayName("예산 카테고리 수정 테스트 : 실패[카테고리 id가 0일 경우]")
    void update_category_fail_when_category_id_is_zero() throws Exception {
        // given
        Long categoryId = 0L;

        // when & then
        assertThatThrownBy(() -> budget.updateCategory(categoryId))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.INVALID_BUDGET_CATEGORY.getMessage());
    }

    @Test
    @DisplayName("예산 카테고리 수정 테스트 : 실패[카테고리 id가 음수 일 경우]")
    void update_category_fail_when_category_id_is_negative() throws Exception {
        // given
        Long categoryId = -1L;

        // when & then
        assertThatThrownBy(() -> budget.updateCategory(categoryId))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.INVALID_BUDGET_CATEGORY.getMessage());
    }
}