package com.project.expensemanger.manager.adaptor.in.api.mapper;

import com.project.expensemanger.manager.adaptor.in.api.dto.response.BudgeIdResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {
    public List<BudgeIdResponse> toIdListDto(List<Long> budgetIdList) {
        return budgetIdList.stream().map(BudgeIdResponse::new).toList();
    }
}
