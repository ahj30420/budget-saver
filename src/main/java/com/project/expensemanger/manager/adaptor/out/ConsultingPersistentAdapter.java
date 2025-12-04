package com.project.expensemanger.manager.adaptor.out;

import com.project.expensemanger.manager.adaptor.out.jpa.budget.BudgetJpaRepository;
import com.project.expensemanger.manager.application.port.out.ConsultingPort;
import com.project.expensemanger.manager.application.service.dto.GetBudgetUsageCondition;
import com.project.expensemanger.manager.domain.consulting.CategoryBudgetUsage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConsultingPersistentAdapter implements ConsultingPort {

    private final BudgetJpaRepository budgetJpaRepository;

    @Override
    public List<CategoryBudgetUsage> getCategoryBudgetUsage(GetBudgetUsageCondition condition) {
        return budgetJpaRepository.findTotalExpenditureByCategoryAndDateAndId(condition)
                .stream()
                .map(c -> {
                    return CategoryBudgetUsage.builder()
                            .categoryId(c.categoryId())
                            .categoryName(c.categoryName())
                            .expenditureAmount(c.expenditureAmount())
                            .budgetAmount(c.budgetAmount())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
