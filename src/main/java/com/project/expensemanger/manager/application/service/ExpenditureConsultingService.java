package com.project.expensemanger.manager.application.service;

import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.application.port.out.ConsultingPort;
import com.project.expensemanger.manager.application.port.out.ExpenditurePort;
import com.project.expensemanger.manager.application.service.dto.GetBudgetUsageCondition;
import com.project.expensemanger.manager.application.service.model.CategoryRecommendtaionModel;
import com.project.expensemanger.manager.application.service.model.TodayBudgetRecommendationModel;
import com.project.expensemanger.manager.application.service.model.TodayCategoryExpenditureModel;
import com.project.expensemanger.manager.application.service.model.TodayExpenditureSummaryModel;
import com.project.expensemanger.manager.domain.budget.Budget;
import com.project.expensemanger.manager.domain.consulting.BudgetConsumption;
import com.project.expensemanger.manager.domain.consulting.CategoryBudgetUsage;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenditureConsultingService {

    private final BudgetPort budgetPort;
    private final ExpenditurePort expenditurePort;
    private final ConsultingPort consultingPort;

    public TodayBudgetRecommendationModel getRecommendation(Long userId) {
        Budget budget = findLatestBudgetOrReturnEmpty(userId);
        if (budget == null) return emptyRecommendation();

        LocalDate today = LocalDate.now();
        LocalDate startDate = budget.getDate();
        LocalDate endDate = startDate.plusDays(29);

        if (isOutOfBudgetPeriod(today, startDate, endDate)) return emptyRecommendation();

        List<CategoryBudgetUsage> usageList = getCategoryBudgetUsage(userId, startDate, endDate);

        long remainingDays = calculateRemainingDays(today, endDate);
        List<CategoryRecommendtaionModel> categoryRecommendations = mapToRecommendations(usageList, remainingDays);

        BudgetConsumption consumption = calculateConsumption(usageList, startDate, today, endDate);

        long todayTotalPossible = sumRecommended(categoryRecommendations);

        return new TodayBudgetRecommendationModel(todayTotalPossible, categoryRecommendations, consumption.getStatus().getMessage());
    }

    public TodayExpenditureSummaryModel getTodayExpenditure(Long userId) {
        Budget budget = findLatestBudgetOrReturnEmpty(userId);

        if (budget == null) return emptyTodayExpenditure();

        LocalDate today = LocalDate.now();
        LocalDate startDate = budget.getDate();
        LocalDate endDate = startDate.plusDays(29);

        if (isOutOfBudgetPeriod(today, startDate, endDate)) return emptyTodayExpenditure();

        List<Expenditure> todayExpenditures = expenditurePort.findTodayExpenditure(userId);
        if (todayExpenditures.isEmpty()) return emptyTodayExpenditure();

        List<Long> categoryIds = todayExpenditures.stream()
                .map(Expenditure::getCategoryId)
                .distinct()
                .toList();

        List<CategoryBudgetUsage> usageList = getCategoryBudgetUsage(userId, startDate, endDate, categoryIds);

        long remainingDays = calculateRemainingDays(today, endDate);

        List<TodayCategoryExpenditureModel> categoryModels = mapToTodayCategoryModels(usageList, todayExpenditures, remainingDays);

        long totalTodayAmount = sumTodayExpenditures(todayExpenditures);

        return new TodayExpenditureSummaryModel(totalTodayAmount, categoryModels);
    }

    private Budget findLatestBudgetOrReturnEmpty(Long userId) {
        return budgetPort.findLastestBudget(userId);
    }

    private boolean isOutOfBudgetPeriod(LocalDate today, LocalDate startDate, LocalDate endDate) {
        return today.isBefore(startDate) || today.isAfter(endDate);
    }

    private List<CategoryBudgetUsage> getCategoryBudgetUsage(Long userId, LocalDate startDate, LocalDate endDate) {
        return getCategoryBudgetUsage(userId, startDate, endDate, null);
    }

    private List<CategoryBudgetUsage> getCategoryBudgetUsage(Long userId, LocalDate startDate, LocalDate endDate, List<Long> categoryIds) {
        GetBudgetUsageCondition condition = GetBudgetUsageCondition.builder()
                .startDate(startDate)
                .endDate(endDate)
                .userId(userId)
                .categoryIdList(categoryIds)
                .build();
        return consultingPort.getCategoryBudgetUsage(condition);
    }

    private long calculateRemainingDays(LocalDate today, LocalDate endDate) {
        return ChronoUnit.DAYS.between(today, endDate) + 1;
    }

    private List<CategoryRecommendtaionModel> mapToRecommendations(List<CategoryBudgetUsage> usageList, long remainingDays) {
        return usageList.stream()
                .map(u -> new CategoryRecommendtaionModel(u.getCategoryId(), u.getCategoryName(), u.getPossibleTodayExpenditure(remainingDays)))
                .toList();
    }

    private long sumRecommended(List<CategoryRecommendtaionModel> categoryRecommendations) {
        return categoryRecommendations.stream().mapToLong(CategoryRecommendtaionModel::recommendedAmount).sum();
    }

    private BudgetConsumption calculateConsumption(List<CategoryBudgetUsage> usageList, LocalDate startDate, LocalDate today, LocalDate endDate) {
        long totalBudget = usageList.stream().mapToLong(CategoryBudgetUsage::getBudgetAmount).sum();
        long totalExpenditure = usageList.stream().mapToLong(CategoryBudgetUsage::getExpenditureAmount).sum();
        long elapsedDays = ChronoUnit.DAYS.between(startDate, today) + 1;
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        return new BudgetConsumption(totalBudget, totalExpenditure, elapsedDays, totalDays);
    }

    private List<TodayCategoryExpenditureModel> mapToTodayCategoryModels(List<CategoryBudgetUsage> usageList, List<Expenditure> todayExpenditures, long remainingDays) {
        return usageList.stream()
                .map(usage -> {
                    long todayAmount = todayExpenditures.stream()
                            .filter(e -> e.getCategoryId().equals(usage.getCategoryId()))
                            .mapToLong(Expenditure::getAmount)
                            .sum();

                    long recommended = usage.getPossibleTodayExpenditure(remainingDays);
                    long risk = calculateRisk(todayAmount, recommended);

                    return new TodayCategoryExpenditureModel(
                            usage.getCategoryId(),
                            usage.getCategoryName(),
                            todayAmount,
                            recommended,
                            risk
                    );
                })
                .toList();
    }

    private long sumTodayExpenditures(List<Expenditure> todayExpenditures) {
        return todayExpenditures.stream().mapToLong(Expenditure::getAmount).sum();
    }

    private long calculateRisk(long todayAmount, long recommended) {
        if (recommended <= 0) return todayAmount > 0 ? 200 : 0;
        return Math.round((double) todayAmount / recommended * 100);
    }

    private TodayBudgetRecommendationModel emptyRecommendation() {
        return new TodayBudgetRecommendationModel(0L, List.of(), "");
    }

    private TodayExpenditureSummaryModel emptyTodayExpenditure() {
        return new TodayExpenditureSummaryModel(0L, List.of());
    }
}
