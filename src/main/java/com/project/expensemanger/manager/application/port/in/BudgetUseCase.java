package com.project.expensemanger.manager.application.port.in;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import jakarta.validation.Valid;
import java.util.List;

public interface BudgetUseCase {
    List<Long> registerBudget(Long userId, RegisterBudgetList requestDto);
}
