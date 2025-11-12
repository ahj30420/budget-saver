package com.project.expensemanger.manager.adaptor.in.api.spec;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterCategoryRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.CategoryIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "카테고리 API", description = "카테고리 관련 API 명세")
public interface CategoryControllerSpec {

    @Operation(
            summary = "카테고리 등록",
            description = "관리자로부터 카테고리 정보를 받아 등록합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = com.project.expensemanger.core.common.response.ApiResponse.class)
            )
    )
    ResponseEntity<CategoryIdResponse> registerCategory(
            @Parameter(description = "카테고리 등록 요청 DTO", required = true)
            RegisterCategoryRequest requestDto
    );
}
