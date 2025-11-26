package com.project.expensemanger.manager.application.model;

import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import java.util.List;
import lombok.Builder;

@Builder
public record ExpenditureListModel(
        List<Expenditure> expenditures,
        Long totalAmount,
        List<ExpenditureByCategoryModel> categoryAmounts
) {}