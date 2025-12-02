package com.project.expensemanger.manager.application.port.out;

import com.project.expensemanger.manager.domain.consulting.CategoryBudgetUsage;
import java.time.LocalDate;
import java.util.List;

public interface ConsultingPort {
    List<CategoryBudgetUsage> getCategoryBudgetUsage(LocalDate startDate, LocalDate endDate, Long userId);
}
