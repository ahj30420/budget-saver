package com.project.expensemanger.manager.application.port.out;

import com.project.expensemanger.manager.application.service.dto.GetBudgetUsageCondition;
import com.project.expensemanger.manager.domain.consulting.CategoryBudgetUsage;
import java.util.List;

public interface ConsultingPort {
    List<CategoryBudgetUsage> getCategoryBudgetUsage(GetBudgetUsageCondition condition);
}
