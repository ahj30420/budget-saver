package com.project.expensemanger.manager.adaptor.in.api.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증/인가 API", description = "인증/인가 관련 API 명세")
public interface AuthControllerSpec {

    @Operation(
            summary = "토큰 갱신",
            description = "refresh_token을 통해 토큰을 갱신합니다."
    )
    ResponseEntity<Void> reissue(
            @Parameter(hidden = true)
            HttpServletRequest request,

            @Parameter(hidden = true)
            HttpServletResponse response
    );

}
