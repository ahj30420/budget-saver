package com.project.expensemanger.manager.application.port.out;

import com.project.expensemanger.manager.domain.expenditure.Expenditure;

public interface ExpenditurePort {
    Expenditure save(Expenditure expenditure);
}
