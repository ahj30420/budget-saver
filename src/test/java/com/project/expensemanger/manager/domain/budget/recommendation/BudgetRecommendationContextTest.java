package com.project.expensemanger.manager.domain.budget.recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.BudgetErrorCode;
import com.project.expensemanger.manager.application.mock.BudgetMock;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.RecommendedBudgetResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BudgetRecommendationContextTest {

    private BudgetRecommendationContext sut;

    private BudgetRecommendationStrategy strategy;

    private BudgetMock budgetMock = new BudgetMock();

    @BeforeEach
    void setUp() {
        strategy = mock(BudgetRecommendationStrategy.class);
        sut = new BudgetRecommendationContext(List.of(strategy));
    }

    @Test
    @DisplayName("예산 추천 전략 선택 context 테스트 : 성공")
    void budget_recommend_context_sccuess_test() throws Exception {
        // given
        given(strategy.getType()).willReturn(RecommendationType.AVERAGE_USER);
        given(strategy.recommend(anyLong(), anyList())).willReturn(budgetMock.recommendedBudgetListMock());

        // when
        List<RecommendedBudgetResult> result = sut.recommend(100000L, budgetMock.categoryBudgetStatMock());

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryName()).isEqualTo("식비");
    }

    @Test
    @DisplayName("예산 추천 전략 선택 context 테스트 : 실패[전략을 찾지 못했을 경우]")
    void budget_recommend_context_fail_when_not_found_strategy() throws Exception {
        // given
        BudgetRecommendationContext emptyContext = new BudgetRecommendationContext(List.of());

        // when & then
        assertThatThrownBy(() -> sut.recommend(1000L, budgetMock.categoryBudgetStatMock()))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.NOT_FOUND_RECOMMEND_STRATEGY.getMessage());
    }

}