package com.project.expensemanger.manager.domain.budget.recommendation;

import com.project.expensemanger.manager.domain.budget.recommendation.vo.CategoryBudgetStat;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.RecommendedBudgetResult;
import java.util.List;

public interface BudgetRecommendationStrategy {
    RecommendationType getType();
    List<RecommendedBudgetResult> recommend(long totalAmount, List<CategoryBudgetStat> categoryStats);
}
