package com.project.expensemanger.manager.application.port.in;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateExpenditureRequest;
import com.project.expensemanger.manager.application.model.ExpenditureDetailView;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import jakarta.validation.Valid;

public interface ExpenditureUseCase {
    Long registerExpenditure(Long userId, RegisterExpenditure requestDto);

    void deleteExpenditure(Long userId, Long expenditureId);

    Expenditure updateExpenditure(Long userId, Long expenditureId, UpdateExpenditureRequest requestDto);

    ExpenditureDetailView getExpenditureDetails(Long userId, Long expenditureId);
}
