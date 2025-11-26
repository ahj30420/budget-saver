package com.project.expensemanger.manager.application.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.ExpenditureErrorCode;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateExpenditureRequest;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.dto.GetExpenditureListCondition;
import com.project.expensemanger.manager.application.model.ExpenditureByCategoryModel;
import com.project.expensemanger.manager.application.model.ExpenditureDetailModel;
import com.project.expensemanger.manager.application.model.ExpenditureListModel;
import com.project.expensemanger.manager.application.port.in.ExpenditureUseCase;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import com.project.expensemanger.manager.application.port.out.ExpenditurePort;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import com.project.expensemanger.manager.domain.expenditure.ExpenditureUpdateCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenditureService implements ExpenditureUseCase {

    private final CategoryPort categoryPort;
    private final ExpenditurePort expenditurePort;

    @Override
    public Long registerExpenditure(Long userId, RegisterExpenditure requestDto) {
        validateCategory(requestDto.categoryId());
        Expenditure expenditure = Expenditure.builder()
                .userId(userId)
                .categoryId(requestDto.categoryId())
                .amount(requestDto.amount())
                .spentAt(requestDto.spentAt())
                .memo(requestDto.memo())
                .excludedFromTotal(false)
                .build();

        Expenditure saved = expenditurePort.save(expenditure);
        return saved.getId();
    }

    private void validateCategory(Long categoryId) {
        categoryPort.findById(categoryId);
    }

    @Override
    @Transactional
    public void deleteExpenditure(Long userId, Long expenditureId) {
        Expenditure expenditure = expenditurePort.findById(expenditureId);
        validateOwner(userId, expenditure);
        expenditurePort.delete(expenditure);
    }

    private void validateOwner(Long userId, Expenditure expenditure) {
        if (!expenditure.getUserId().equals(userId)) {
            throw new BaseException(ExpenditureErrorCode.EXPENDITURE_FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public Expenditure updateExpenditure(Long userId, Long expenditureId, UpdateExpenditureRequest requestDto) {
        Expenditure expenditure = expenditurePort.findById(expenditureId);
        validateCategory(requestDto.categoryId());
        validateOwner(userId, expenditure);
        update(expenditure, requestDto);
        expenditurePort.update(expenditure);
        return expenditure;
    }

    private void update(Expenditure expenditure, UpdateExpenditureRequest request) {
        var cmd = new ExpenditureUpdateCommand(
                request.amount(),
                request.spentAt(),
                request.memo(),
                request.categoryId(),
                request.excludedFromTotal()
        );

        expenditure.update(cmd);
    }

    @Override
    public ExpenditureDetailModel getExpenditureDetails(Long userId, Long expenditureId) {
        ExpenditureDetailModel detail = expenditurePort.getDetails(expenditureId);

        if (!detail.userId().equals(userId)) {
            throw new BaseException(ExpenditureErrorCode.EXPENDITURE_FORBIDDEN);
        }

        return detail;
    }

    @Override
    public ExpenditureListModel getExpenditureListByCondition(GetExpenditureListCondition conditionDto) {
        List<Expenditure> allExpenditureByCondition = expenditurePort.findAllExpenditureByCondition(conditionDto);
        List<ExpenditureByCategoryModel> totalExpenditureByCategory =
                expenditurePort.findTotalExpenditureByCategory(conditionDto);

        Long totalAmount = 0L;
        for (ExpenditureByCategoryModel model : totalExpenditureByCategory) {
            Long categoryAmount = isEmpty(model.totalAmount()) ? 0L : model.totalAmount();
            totalAmount += categoryAmount;
        }

        return ExpenditureListModel.builder()
                .expenditures(allExpenditureByCondition)
                .totalAmount(totalAmount)
                .categoryAmounts(totalExpenditureByCategory)
                .build();
    }
}
