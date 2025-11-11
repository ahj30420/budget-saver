package com.project.expensemanger.manager.adaptor.in.api.spec;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.SignupRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.UserIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 API", description = "회원 관련 API 명세")
public interface UserControllerSpec {

    @Operation(
            summary = "회원가입",
            description = "사용자로부터 회원가입 정보를 받아 회원을 등록합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = com.project.expensemanger.core.common.response.ApiResponse.class)
            )
    )
    ResponseEntity<UserIdResponse> signup(
            @Parameter(description = "회원가입 요청 DTO", required = true)
            SignupRequest request
    );
}
