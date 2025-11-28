package com.project.expensemanger.manager.adaptor.in.api.spec;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateBudgetRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.BudgeIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.BudgetResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.RecommendBudgetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "예산 API", description = "예산 관련 API 명세")
public interface BudgetControllerSpec {

    @Operation(
            summary = "예산 등록",
            description = "사용자로부터 예산 정보를 받아 등록합니다."
    )
    ResponseEntity<List<BudgeIdResponse>> registerBudget(
            @Parameter(hidden = true)
            Long userId,

            @Parameter(description = "예산 목록 등록 요청 DTO", required = true)
            RegisterBudgetList requestDto
    );


    @Operation(
            summary = "예산 단건 조회",
            description = "특정 예산에 대한 정보를 조회합니다."
    )
    ResponseEntity<BudgetResponse> getBudget(
            @Parameter(hidden = true)
            Long userId,

            @Parameter(
                    description = "조회할 예산의 ID",
                    example = "1"
            )
            Long budgetId
    );


    @Operation(
            summary = "사용자 예산 조회",
            description = "사용자의 모든 예산에 대한 정보를 조회합니다."
    )
    ResponseEntity<List<BudgetResponse>> getBudgetList(
            @Parameter(hidden = true)
            Long userId
    );


    @Operation(
            summary = "예산 수정",
            description = "예산 정보를 수정합니다."
    )
    ResponseEntity<BudgeIdResponse> updateBudget(
            @Parameter(hidden = true)
            Long userId,

            @Parameter(
                    description = "수정할 예산의 ID",
                    example = "1"
            )
            Long budgetId,

            @Parameter(description = "예산 수정 요청 DTO", required = true)
            UpdateBudgetRequest requestDto
    );


    @Operation(
            summary = "예산 삭제",
            description = "예산 정보를 삭제합니다."
    )
    ResponseEntity<Void> deleteBudget(
            @Parameter(hidden = true)
            Long userId,

            @Parameter(
                    description = "삭제할 예산의 ID",
                    example = "1"
            )
            Long budgetId
    );


    @Operation(
            summary = "예산 추천",
            description = "예산을 카테고리별로 추천 받습니다."
    )
    ResponseEntity<List<RecommendBudgetResponse>> getRecommendBudget(
            @Parameter(
                    description = "전체 예산",
                    example = "100000"
            )
            Long amount
    );

    @Operation(
            summary = "예산 추천 V2",
            description = "예산을 카테고리별로 추천 받습니다."
    )
    ResponseEntity<List<RecommendBudgetResponse>> getRecommendBudgetV2(
            @Parameter(
                    description = "전체 예산",
                    example = "100000"
            )
            Long amount
    );
}
