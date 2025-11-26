package com.project.expensemanger.manager.adaptor.in.api.spec;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterCategoryRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.CategoryIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.GetCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "카테고리 API", description = "카테고리 관련 API 명세")
public interface CategoryControllerSpec {

    @Operation(
            summary = "카테고리 등록",
            description = "관리자로부터 카테고리 정보를 받아 등록합니다."
    )
    ResponseEntity<CategoryIdResponse> registerCategory(
            @Parameter(description = "카테고리 등록 요청 DTO", required = true)
            RegisterCategoryRequest requestDto
    );


    @Operation(
            summary = "카테고리 단건 조회",
            description = "카테고리 하나를 조회합니다."
    )
    ResponseEntity<GetCategoryResponse> getCategory(
            @Parameter(
                    description = "조회할 카테고리의 ID",
                    example = "1"
            )
            Long categoryId
    );


    @Operation(
            summary = "카테고리 전체 조회",
            description = "카테고리 전체를 조회합니다."
    )
    ResponseEntity<List<GetCategoryResponse>> getAllCategories();
}
