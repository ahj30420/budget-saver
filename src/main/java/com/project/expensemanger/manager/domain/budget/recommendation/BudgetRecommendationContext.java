package com.project.expensemanger.manager.domain.budget.recommendation;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.BudgetErrorCode;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.CategoryBudgetStat;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.RecommendedBudgetResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetRecommendationContext {

    private final List<BudgetRecommendationStrategy> strategies;

    public List<RecommendedBudgetResult> recommend(long totalAmount, List<CategoryBudgetStat> categoryStats) {
        BudgetRecommendationStrategy strategy = strategies.stream()
                .filter(s -> s.getType() == determineStrategyType(categoryStats))
                .findFirst()
                .orElseThrow(() -> new BaseException(BudgetErrorCode.NOT_FOUND_RECOMMEND_STRATEGY));

        return strategy.recommend(totalAmount, categoryStats);
    }

    private RecommendationType determineStrategyType(List<CategoryBudgetStat> categoryStats) {
        return RecommendationType.AVERAGE_USER;
    }
}
