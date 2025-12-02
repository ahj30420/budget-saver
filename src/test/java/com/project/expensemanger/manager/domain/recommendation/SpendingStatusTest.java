package com.project.expensemanger.manager.domain.recommendation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SpendingStatusTest {

    @Test
    @DisplayName("지출 현황 계산 테스트 : GOOD")
    void get_spending_status_GOOD_test() throws Exception {
        // given
        double rate = 75.1;

        // when
        SpendingStatus result = SpendingStatus.fromRate(rate);

        // then
        assertThat(result).isEqualTo(SpendingStatus.GOOD);
    }

    @Test
    @DisplayName("지출 현황 계산 테스트 : NORMAL")
    void get_spending_status_NORMAL_test() throws Exception {
        // given
        double rate = 94.22;

        // when
        SpendingStatus result = SpendingStatus.fromRate(rate);

        // then
        assertThat(result).isEqualTo(SpendingStatus.NORMAL);
    }

    @Test
    @DisplayName("지출 현황 계산 테스트 : WARN")
    void get_spending_status_WARN_test() throws Exception {
        // given
        double rate = 110.23;

        // when
        SpendingStatus result = SpendingStatus.fromRate(rate);

        // then
        assertThat(result).isEqualTo(SpendingStatus.WARN);
    }

    @Test
    @DisplayName("지출 현황 계산 테스트 : OVER")
    void get_spending_status_OVER_test() throws Exception {
        // given
        double rate = 125.13;

        // when
        SpendingStatus result = SpendingStatus.fromRate(rate);

        // then
        assertThat(result).isEqualTo(SpendingStatus.OVER);
    }

}