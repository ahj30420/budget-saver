package com.project.expensemanger.manager.application.service;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.application.port.in.ExpenditureUseCase;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import com.project.expensemanger.manager.application.port.out.ExpenditurePort;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
