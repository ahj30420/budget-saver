package com.project.expensemanger.manager.domain.recommendation;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.expensemanger.manager.application.mock.CategoryBudgetUsageMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryBudgetUsageTest {

    CategoryBudgetUsageMock mock = new CategoryBudgetUsageMock();
    CategoryBudgetUsage sut;

    @Test
    @DisplayName("남은 예산 계산 테스트")
    void get_remaining_budget_amount_test() throws Exception {
        // when
        sut = mock.domainMock();
        long result = sut.getRemainingBudgetAmount();

        // then
        assertThat(result).isEqualTo(30000L);
    }

    @Test
    @DisplayName("하루 가능 지출 계산 테스트 : 정상 케이스")
    void get_possible_today_expenditure_normal_test() throws Exception {
        // given
        sut = mock.domainMock();
        long remainingDays = 5L;

        // when
        long result = sut.getPossibleTodayExpenditure(remainingDays);

        // then
        assertThat(result).isEqualTo(6000L);
    }

    @Test
    @DisplayName("하루 가능 지출 계산 테스트 : 남은 예산 0 이하")
    void get_possible_today_expenditure_zero_budget_test() throws Exception {
        // given
        sut = mock.zeroBudgetMock();
        long remainingDays = 3L;

        // when
        long result = sut.getPossibleTodayExpenditure(remainingDays);

        // then
        assertThat(result).isEqualTo(5000L);
    }

    @Test
    @DisplayName("하루 가능 지출 계산 테스트 - 100단위 반올림 확인")
    void get_possible_today_expenditure_rounding_test() throws Exception {
        // given
        sut = mock.roundingMock();
        long remainingDays = 2L;

        // when
        long result = sut.getPossibleTodayExpenditure(remainingDays);

        // then
        assertThat(result).isEqualTo(3300L);
    }
}