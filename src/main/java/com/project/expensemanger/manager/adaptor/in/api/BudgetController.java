package com.project.expensemanger.manager.adaptor.in.api;

import com.project.expensemanger.core.common.annotation.CurrentUser;
import com.project.expensemanger.core.common.util.UrlCreator;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateBudgetRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.BudgeIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.BudgetResponse;
import com.project.expensemanger.manager.adaptor.in.api.mapper.BudgetMapper;
import com.project.expensemanger.manager.adaptor.in.api.spec.BudgetControllerSpec;
import com.project.expensemanger.manager.application.port.in.BudgetUseCase;
import com.project.expensemanger.manager.domain.budget.Budget;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Override
    @GetMapping("/api/budget/{budgetId}")
    public ResponseEntity<BudgetResponse> getBudget(
            @CurrentUser Long userId,
            @PathVariable Long budgetId
    ) {
        Budget budget = budgetUseCase.getBudget(userId, budgetId);
        return ResponseEntity.ok().body(budgetMapper.toBudgetDto(budget));
    }

    @Override
    @GetMapping("/api/budget/list")
    public ResponseEntity<List<BudgetResponse>> getBudgetList(
            @CurrentUser Long userId
    ) {
        List<Budget> budgets = budgetUseCase.getBudgetList(userId);
        return ResponseEntity.ok().body(budgetMapper.toBudgetListDto(budgets));
    }

    @Override
    @PatchMapping("/api/budget/{budgetId}")
    public ResponseEntity<BudgeIdResponse> updateBudget(
            @CurrentUser Long userId,
            @PathVariable Long budgetId,
            @RequestBody @Valid UpdateBudgetRequest requestDto
    ) {
        Budget updateBudget = budgetUseCase.updateBudget(userId, budgetId, requestDto);
        URI location = UrlCreator.createUri(DEFAULT, updateBudget.getId());
        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, location.toString())
                .body(budgetMapper.toIdDto(updateBudget));
    }
}
