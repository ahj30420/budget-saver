package com.project.expensemanger.manager.application.mock;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudget;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.domain.budget.Budget;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BudgetMock {

    private final Long id = 1L;
    private final LocalDate date = LocalDate.of(2025, 11, 11);
    private final Long amount = 2000L;
    private final Long categoryId = 1L;
    private final Long userId = 1L;

    private final List<Long> idList = List.of(1L, 2L, 3L);
    private final List<Long> amountList = List.of(2000L, 1000L, 3000L);
    private final List<Long> categoryIdList = List.of(1L, 2L, 3L);
    private final List<Long> userIdList = List.of(1L, 2L, 3L);

    public Budget domainMock() {
        return Budget.builder()
                .id(id)
                .categoryId(categoryId)
                .userId(userId)
                .amount(amount)
                .date(date)
                .build();
    }

    public List<Budget> domainListMock() {
        List<Budget> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Budget budget = Budget.builder()
                    .id(idList.get(i))
                    .userId(userIdList.get(i))
                    .categoryId(categoryIdList.get(i))
                    .amount(amountList.get(i))
                    .date(date)
                    .build();
            result.add(budget);
        }
        return result;
    }

    public List<Long> domainIdListMock() {
        return idList;
    }

    public RegisterBudgetList RegisterRequestDto() {
        List<RegisterBudget> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            RegisterBudget registerBudget = new RegisterBudget(date, amountList.get(i), categoryIdList.get(i));
            result.add(registerBudget);
        }
        return new RegisterBudgetList(result);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getAmount() {
        return amount;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getUserId() {
        return userId;
    }
}
