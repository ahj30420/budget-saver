package com.project.expensemanger.manager.adaptor.in.api.mapper;

import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureDetailsResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.GetCategoryResponse;
import com.project.expensemanger.manager.application.model.ExpenditureDetailView;
import org.springframework.stereotype.Component;

@Component
public class ExpenditureMapper {
    public ExpenditureIdResponse toIdDto(Long expenditureId) {
        return new ExpenditureIdResponse(expenditureId);
    }

    public ExpenditureDetailsResponse toExpenditureDetailsDto(ExpenditureDetailView detailView) {
        return new ExpenditureDetailsResponse(
                detailView.expenditureId(),
                detailView.spentAt(),
                detailView.amount(),
                detailView.memo(),
                new GetCategoryResponse(
                        detailView.categoryId(),
                        detailView.categoryName()
                )
        );
    }
}
