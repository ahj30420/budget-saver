package com.project.expensemanger.manager.application.mock;

import com.project.expensemanger.manager.domain.recommendation.BudgetConsumption;
import org.springframework.stereotype.Component;

@Component
public class BudgetConsumptionMock {

    private final long totalBudget = 10000;
    private final long totalSpent = 6000;
    private final long elapsedDays = 7;
    private final long totalDays = 10;


    public BudgetConsumption domainMock() {
        return BudgetConsumption.builder()
                .totalBudget(totalBudget)
                .totalSpent(totalSpent)
                .elapsedDays(elapsedDays)
                .totalDays(totalDays)
                .build();
    }

    public BudgetConsumption zeroBudgetMock() {
        return BudgetConsumption.builder()
                .totalBudget(0L)
                .totalSpent(totalSpent)
                .elapsedDays(elapsedDays)
                .totalDays(totalDays)
                .build();
    }

    public BudgetConsumption zeroDaysMock() {
        return BudgetConsumption.builder()
                .totalBudget(totalBudget)
                .totalSpent(totalSpent)
                .elapsedDays(elapsedDays)
                .totalDays(0L)
                .build();
    }

    public BudgetConsumption overSpentMock() {
        return BudgetConsumption.builder()
                .totalBudget(totalBudget)
                .totalSpent(9000L)
                .elapsedDays(elapsedDays)
                .totalDays(totalDays)
                .build();
    }

}
