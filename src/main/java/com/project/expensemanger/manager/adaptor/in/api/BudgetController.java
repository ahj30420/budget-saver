package com.project.expensemanger.manager.adaptor.in.api;

import com.project.expensemanger.core.common.annotation.CurrentUser;
import com.project.expensemanger.core.common.util.UrlCreator;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.BudgeIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.mapper.BudgetMapper;
import com.project.expensemanger.manager.adaptor.in.api.spec.BudgetControllerSpec;
import com.project.expensemanger.manager.application.port.in.BudgetUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BudgetController implements BudgetControllerSpec {

    private static final String DEFAULT = "/api/budget";
    private final BudgetUseCase budgetUseCase;
    private final BudgetMapper budgetMapper;

    @Override
    @PostMapping("/api/budget/list")
    public ResponseEntity<List<BudgeIdResponse>> registerBudget(
            @CurrentUser Long userId,
            @Valid @RequestBody RegisterBudgetList requestDto
    ) {
        List<Long> budgetIdList =  budgetUseCase.registerBudget(userId, requestDto);
        URI location = UrlCreator.createCollectionUri(DEFAULT + "/list");
        return ResponseEntity.created(location).body(budgetMapper.toIdListDto(budgetIdList));
    }

}
