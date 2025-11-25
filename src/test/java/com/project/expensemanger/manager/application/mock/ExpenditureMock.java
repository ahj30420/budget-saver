package com.project.expensemanger.manager.application.mock;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class ExpenditureMock {

    private final Long id = 1L;
    private final LocalDateTime spentAt = LocalDateTime.of(2026, 11, 25, 12, 30, 10);
    private final Long amount = 1000L;
    private final String memo = "메모 테스트";
    private final boolean excludedFromTotal = false;
    private final Long categoryId = 1L;
    private final Long userId = 1L;

    public Expenditure toDomain() {
        return Expenditure.builder()
                .id(id)
                .spentAt(spentAt)
                .amount(amount)
                .memo(memo)
                .excludedFromTotal(excludedFromTotal)
                .categoryId(categoryId)
                .userId(userId)
                .build();
    }

    public RegisterExpenditure RegisterRequestDto() {
        return new RegisterExpenditure(
                categoryId,
                amount,
                spentAt,
                memo
        );
    }

    public Long getId() {
        return id;
    }
}
