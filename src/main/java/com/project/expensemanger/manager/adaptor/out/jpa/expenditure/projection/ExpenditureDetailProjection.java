package com.project.expensemanger.manager.adaptor.out.jpa.expenditure.projection;

import java.time.LocalDateTime;

public interface ExpenditureDetailProjection {
    Long getExpenditureId();
    Long getUserId();
    LocalDateTime getSpendAt();
    Long getAmount();
    String getMemo();
    Long getCategoryId();
    String getCategoryName();
}
