package com.project.expensemanger.manager.domain.budget.recommendation;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.expensemanger.manager.domain.budget.recommendation.vo.CategoryBudgetStat;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.RecommendedBudgetResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AverageUserBudgetRecommendationTest {

    private AverageUserBudgetRecommendation sut = new AverageUserBudgetRecommendation();

    @Test
    @DisplayName("추천 금액 계산 및 기타 합산 테스트 : 성공")
    void recommend_success_test() throws Exception {
        // given
        List<CategoryBudgetStat> categoryBudgetStats = List.of(
                new CategoryBudgetStat(1L, "기타", 300L),
                new CategoryBudgetStat(2L, "식비", 5000L),
                new CategoryBudgetStat(3L, "교통", 200L)
        );

        Long totalAmount = 1000L;

        // when
        List<RecommendedBudgetResult> result = sut.recommend(totalAmount, categoryBudgetStats);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryName()).isEqualTo("식비");
        assertThat(result.get(0).getRecommendedAmount()).isEqualTo(909);
        assertThat(result.get(1).getCategoryName()).isEqualTo("기타");
        assertThat(result.get(1).getRecommendedAmount()).isEqualTo(91);
    }
}