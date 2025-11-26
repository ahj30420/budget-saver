package com.project.expensemanger.manager.application.port.in;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateExpenditureRequest;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.dto.GetExpenditureListCondition;
import com.project.expensemanger.manager.application.service.model.ExpenditureDetailModel;
import com.project.expensemanger.manager.application.service.model.ExpenditureListModel;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;

public interface ExpenditureUseCase {
    Long registerExpenditure(Long userId, RegisterExpenditure requestDto);

    void deleteExpenditure(Long userId, Long expenditureId);

    Expenditure updateExpenditure(Long userId, Long expenditureId, UpdateExpenditureRequest requestDto);

    ExpenditureDetailModel getExpenditureDetails(Long userId, Long expenditureId);

    ExpenditureListModel getExpenditureListByCondition(GetExpenditureListCondition conditionDto);
}
