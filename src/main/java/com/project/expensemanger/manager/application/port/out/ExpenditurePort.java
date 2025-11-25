package com.project.expensemanger.manager.application.port.out;

import com.project.expensemanger.manager.application.model.ExpenditureDetailModel;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;

public interface ExpenditurePort {
    Expenditure save(Expenditure expenditure);

    Expenditure findById(Long expenditureId);

    void delete(Expenditure expenditure);

    void update(Expenditure expenditure);

    ExpenditureDetailModel getDetails(Long expenditureId);
}
