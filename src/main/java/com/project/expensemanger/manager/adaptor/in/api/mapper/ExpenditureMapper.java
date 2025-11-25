package com.project.expensemanger.manager.adaptor.in.api.mapper;

import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureIdResponse;
import org.springframework.stereotype.Component;

@Component
public class ExpenditureMapper {
    public ExpenditureIdResponse toIdDto(Long expenditureId) {
        return new ExpenditureIdResponse(expenditureId);
    }
}
