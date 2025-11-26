package com.project.expensemanger.manager.adaptor.out.jpa.expenditure;

import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.dto.GetExpenditureListCondition;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.entity.ExpenditureJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.projection.ExpenditureByCategoryProjection;
import java.util.List;

public interface ExpenditureCustomRepository  {
    List<ExpenditureJpaEntity> findAllExpenditureByCondition(GetExpenditureListCondition condition);

    List<ExpenditureByCategoryProjection> findTotalExpenditureByCategory(GetExpenditureListCondition conditionDto);
}
