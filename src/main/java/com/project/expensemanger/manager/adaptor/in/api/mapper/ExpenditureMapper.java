package com.project.expensemanger.manager.adaptor.in.api.mapper;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.GetExpenditureListRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureDetailsResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureListResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureListResponse.CategoryAmount;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureListResponse.ExpenditureInfo;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.GetCategoryResponse;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.dto.GetExpenditureListCondition;
import com.project.expensemanger.manager.application.service.model.ExpenditureByCategoryModel;
import com.project.expensemanger.manager.application.service.model.ExpenditureDetailModel;
import com.project.expensemanger.manager.application.service.model.ExpenditureListModel;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExpenditureMapper {
    public ExpenditureIdResponse toIdDto(Long expenditureId) {
        return new ExpenditureIdResponse(expenditureId);
    }

    public ExpenditureDetailsResponse toExpenditureDetailsDto(ExpenditureDetailModel detailView) {
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

    public GetExpenditureListCondition toConditionDto(Long userId, GetExpenditureListRequest requestDto) {
        return GetExpenditureListCondition.builder()
                .categoryId(requestDto.categoryId())
                .userId(userId)
                .startDate(requestDto.startDate())
                .endDate(requestDto.endDate())
                .minAmount(requestDto.minAmount())
                .maxAmount(requestDto.maxAmount())
                .build();
    }

    public ExpenditureListResponse toExpenditureListResponse(ExpenditureListModel model) {
        List<ExpenditureInfo> expenditures = new ArrayList<>();
        for (Expenditure m : model.expenditures()) {
            ExpenditureInfo build = ExpenditureInfo.builder()
                    .expenditureId(m.getId())
                    .categoryId(m.getCategoryId())
                    .spendAt(m.getSpentAt())
                    .amount(m.getAmount())
                    .build();
            expenditures.add(build);
        }

        List<CategoryAmount> categoryAmounts = new ArrayList<>();
        for (ExpenditureByCategoryModel m : model.categoryAmounts()){
            CategoryAmount build = CategoryAmount.builder()
                    .categoryId(m.categoryId())
                    .categoryName(m.categoryName())
                    .totalAmount(m.totalAmount())
                    .build();
            categoryAmounts.add(build);
        }

        return ExpenditureListResponse.builder()
                .expenditures(expenditures)
                .totalAmount(model.totalAmount())
                .categoryAmounts(categoryAmounts)
                .build();
    }
}
