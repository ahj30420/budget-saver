package com.project.expensemanger.manager.application.port.out;

import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.dto.GetExpenditureListCondition;
import com.project.expensemanger.manager.application.service.model.ExpenditureByCategoryModel;
import com.project.expensemanger.manager.application.service.model.ExpenditureDetailModel;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import java.util.List;

public interface ExpenditurePort {
    Expenditure save(Expenditure expenditure);

    Expenditure findById(Long expenditureId);

    void delete(Expenditure expenditure);

    void update(Expenditure expenditure);

    ExpenditureDetailModel getDetails(Long expenditureId);

    List<Expenditure> findAllExpenditureByCondition(GetExpenditureListCondition conditionDto);

    List<ExpenditureByCategoryModel> findTotalExpenditureByCategory(GetExpenditureListCondition conditionDto);

    List<Expenditure> findTodayExpenditure(Long userId);
}
