package com.project.expensemanger.manager.domain.consulting;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BudgetConsumption {

    private final long totalBudget;
    private final long totalSpent;
    private final long elapsedDays;
    private final long totalDays;

    @Builder
    public BudgetConsumption(long totalBudget, long totalSpent, long elapsedDays, long totalDays) {
        this.totalBudget = totalBudget;
        this.totalSpent = totalSpent;
        this.elapsedDays = elapsedDays;
        this.totalDays = totalDays;
    }

    public double getConsumptionRate() {
        if (totalBudget <= 0 || totalDays <= 0) return 0.0;
        double expectedSpend = (double) totalBudget * elapsedDays / totalDays;
        return (double) totalSpent / expectedSpend * 100;
    }

    public SpendingStatus getStatus() {
        return SpendingStatus.fromRate(getConsumptionRate());
    }
}
