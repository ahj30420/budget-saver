package com.project.expensemanger.manager.application.service;

import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.application.port.out.ConsultingPort;
import com.project.expensemanger.manager.application.service.model.CategoryRecommendtaionModel;
import com.project.expensemanger.manager.application.service.model.TodayBudgetRecommendationModel;
import com.project.expensemanger.manager.domain.budget.Budget;
import com.project.expensemanger.manager.domain.consulting.BudgetConsumption;
import com.project.expensemanger.manager.domain.consulting.CategoryBudgetUsage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenditureConsultingService {

    private final BudgetPort budgetPort;
    private final ConsultingPort consultingPort;

    public TodayBudgetRecommendationModel getRecommendation(Long userId) {

        Budget lastestBudget = budgetPort.findLastestBudget(userId);

        if (lastestBudget == null) {
            return emptyRecommendation();
        }

        LocalDate now = LocalDate.now();
        LocalDate startDate = lastestBudget.getDate();
        LocalDate endDate = startDate.plusDays(29);

        if (startDate.isAfter(now) || endDate.isBefore(now)) {
            return emptyRecommendation();
        }

        List<CategoryBudgetUsage> categoryBudgetUsageList = consultingPort.getCategoryBudgetUsage(startDate, endDate,
                userId);

        long remainingDays = ChronoUnit.DAYS.between(now, endDate) + 1;

        List<CategoryRecommendtaionModel> categoryRecommendations = categoryBudgetUsageList.stream()
                .map(c -> mapToRecommendation(c, remainingDays))
                .collect(Collectors.toList());

        long totalBudgetAmount = categoryBudgetUsageList.stream()
                .mapToLong(CategoryBudgetUsage::getBudgetAmount)
                .sum();

        long totalExpenditureAmount = categoryBudgetUsageList.stream()
                .mapToLong(CategoryBudgetUsage::getExpenditureAmount)
                .sum();

        long elapsedDays = ChronoUnit.DAYS.between(startDate, now) + 1;
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        BudgetConsumption consumption = new BudgetConsumption(totalBudgetAmount, totalExpenditureAmount, elapsedDays, totalDays);

        long todayTotalPossible = categoryRecommendations.stream()
                .mapToLong(CategoryRecommendtaionModel::getRecommendedAmount)
                .sum();

        return TodayBudgetRecommendationModel.builder()
                .todayTotalPossible(todayTotalPossible)
                .categories(categoryRecommendations)
                .message(consumption.getStatus().getMessage())
                .build();
    }

    private CategoryRecommendtaionModel mapToRecommendation(CategoryBudgetUsage usage, long remainingDays) {
        return CategoryRecommendtaionModel.builder()
                .categoryId(usage.getCategoryId())
                .categoryName(usage.getCategoryName())
                .recommendedAmount(usage.getPossibleTodayExpenditure(remainingDays))
                .build();
    }


    private TodayBudgetRecommendationModel emptyRecommendation() {
        return TodayBudgetRecommendationModel.builder()
                .todayTotalPossible(0L)
                .categories(Collections.emptyList())
                .message("")
                .build();
    }

}
