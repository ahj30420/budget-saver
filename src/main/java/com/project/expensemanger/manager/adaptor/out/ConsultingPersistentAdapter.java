package com.project.expensemanger.manager.adaptor.out;

import com.project.expensemanger.manager.adaptor.out.jpa.budget.BudgetJpaRepository;
import com.project.expensemanger.manager.application.port.out.ConsultingPort;
import com.project.expensemanger.manager.domain.consulting.CategoryBudgetUsage;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConsultingPersistentAdapter implements ConsultingPort {

    private final BudgetJpaRepository budgetJpaRepository;

    @Override
    public List<CategoryBudgetUsage> getCategoryBudgetUsage(LocalDate startDate, LocalDate endDate, Long userId) {
        return budgetJpaRepository.findTotalExpenditureByCategoryAndDateAndId(startDate, endDate, userId)
                .stream()
                .map(c -> {
                    return CategoryBudgetUsage.builder()
                            .categoryId(c.getCategoryId())
                            .categoryName(c.getCategoryName())
                            .expenditureAmount(c.getExpenditureAmount())
                            .budgetAmount(c.getBudgetAmount())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
