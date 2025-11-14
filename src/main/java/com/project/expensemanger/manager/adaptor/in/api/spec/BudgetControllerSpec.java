package com.project.expensemanger.manager.adaptor.in.api.spec;

import com.project.expensemanger.core.common.annotation.CurrentUser;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.BudgeIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.BudgetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "예산 API", description = "예산 관련 API 명세")
public interface BudgetControllerSpec {

    @Operation(
            summary = "예산 등록",
            description = "사용자로부터 예산 정보를 받아 등록합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = com.project.expensemanger.core.common.response.ApiResponse.class)
            )
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
    @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = com.project.expensemanger.core.common.response.ApiResponse.class)
            )
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
    @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = com.project.expensemanger.core.common.response.ApiResponse.class)
            )
    )
    ResponseEntity<List<BudgetResponse>> getBudgetList(
            @Parameter(hidden = true)
            Long userId
    );

}
