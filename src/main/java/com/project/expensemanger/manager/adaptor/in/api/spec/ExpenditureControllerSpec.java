package com.project.expensemanger.manager.adaptor.in.api.spec;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateExpenditureRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureDetailsResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "지출 API", description = "지출 관련 API 명세")
public interface ExpenditureControllerSpec {

    @Operation(
            summary = "지출 등록",
            description = "사용자로부터 지출 정보를 받아 등록합니다."
    )
    ResponseEntity<ExpenditureIdResponse> registerExpenditure(
            @Parameter(hidden = true)
            Long userId,

            @Parameter(description = "지출 등록 요청 DTO", required = true)
            RegisterExpenditure requestDto
    );


    @Operation(
            summary = "지출 삭제",
            description = "지출 정보를 삭제합니다."
    )
    ResponseEntity<Void> deleteExpenditure(
            @Parameter(hidden = true)
            Long userId,

            @Parameter(
                    description = "삭제할 지출의 ID",
                    example = "1"
            )
            Long expenditureId
    );


    @Operation(
            summary = "지출 수정",
            description = "지출 정보를 수정합니다."
    )
    ResponseEntity<ExpenditureIdResponse> updateExpenditure(
            @Parameter(hidden = true)
            Long userId,

            @Parameter(
                    description = "삭제할 지출의 ID",
                    example = "1"
            )
            Long expenditureId,

            @Parameter(description = "지출 수정 요청 DTO", required = true)
            UpdateExpenditureRequest requestDto
    );


    @Operation(
            summary = "지출 상세 조회",
            description = "지출 상세 정보를 조회합니다."
    )
    ResponseEntity<ExpenditureDetailsResponse> getExpenditure(
            @Parameter(hidden = true)
            Long userId,

            @Parameter(
                    description = "삭제할 지출의 ID",
                    example = "1"
            )
            Long expenditureId
    );
}
