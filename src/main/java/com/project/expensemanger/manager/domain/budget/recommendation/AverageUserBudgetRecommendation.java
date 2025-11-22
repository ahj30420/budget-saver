package com.project.expensemanger.manager.domain.budget.recommendation;

import com.project.expensemanger.manager.domain.budget.recommendation.vo.CategoryBudgetStat;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.RecommendedBudgetResult;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AverageUserBudgetRecommendation implements BudgetRecommendationStrategy {

    private static final double RATE_UNDER_LIMIT = 0.1;
    private static final String CATEGORY_OTHER = "기타";

    @Override
    public RecommendationType getType() {
        return RecommendationType.AVERAGE_USER;
    }

    @Override
    public List<RecommendedBudgetResult> recommend(long totalAmount, List<CategoryBudgetStat> categoryStats) {
        long total = categoryStats.stream().mapToLong(CategoryBudgetStat::getAmount).sum();

        List<RecommendedBudgetResult> result = new ArrayList<>();
        long othersAccumulated = 0;

        for (CategoryBudgetStat c : categoryStats) {
            double ratio = (double) c.getAmount() / total;
            if (ratio <= RATE_UNDER_LIMIT || c.getCategoryName().equals(CATEGORY_OTHER)) {
                othersAccumulated += Math.round(totalAmount * ratio);
            } else {
                result.add(new RecommendedBudgetResult(c.getCategoryId(), c.getCategoryName(),
                        Math.round(totalAmount * ratio)));
            }
        }

        if (othersAccumulated > 0) {
            result.add(new RecommendedBudgetResult(1L, CATEGORY_OTHER, othersAccumulated));
        }

        return result;
    }
}
