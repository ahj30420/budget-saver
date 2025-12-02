package com.project.expensemanger.manager.domain.recommendation;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.expensemanger.manager.application.mock.BudgetConsumptionMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BudgetConsumptionTest {

    private final BudgetConsumptionMock mock = new BudgetConsumptionMock();
    private BudgetConsumption sut;

    @Test
    @DisplayName("소비율 계산 테스트")
    void get_consumption_rate_test() throws Exception {
        // given
        sut = mock.domainMock();

        // when
        double result = sut.getConsumptionRate();

        // then
        assertThat(Math.round(result * 100.0) / 100.0).isEqualTo(85.71);
    }

    @Test
    @DisplayName("소비율 계산 테스트 - totalBudget 0")
    void get_consumption_rate_zero_budget_test() {
        // given
        sut = mock.zeroBudgetMock();

        // when
        double result = sut.getConsumptionRate();

        // then
        assertThat(result).isEqualTo(0.0);
    }

    @Test
    @DisplayName("소비율 계산 테스트 - totalDays 0")
    void get_consumption_rate_zero_days_test() {
        // given
        sut = mock.zeroDaysMock();

        // when
        double result = sut.getConsumptionRate();

        // then
        assertThat(result).isEqualTo(0.0);
    }

    @Test
    @DisplayName("소비율 계산 테스트 - 예상보다 초과 소비")
    void get_consumption_rate_over_spent_test() {
        // given
        sut = mock.overSpentMock();

        // when
        double result = sut.getConsumptionRate();

        // then
        assertThat(Math.round(result * 100.0) / 100.0).isEqualTo(128.57);
    }
}