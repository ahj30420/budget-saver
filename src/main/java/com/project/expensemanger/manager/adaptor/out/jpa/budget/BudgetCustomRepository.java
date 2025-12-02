package com.project.expensemanger.manager.adaptor.out.jpa.budget;

import com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetUsageProjection;
import com.project.expensemanger.manager.application.service.dto.GetBudgetUsageCondition;
import java.util.List;

public interface BudgetCustomRepository {

    List<CategoryBudgetUsageProjection> findTotalExpenditureByCategoryAndDateAndId(GetBudgetUsageCondition condition);
}
