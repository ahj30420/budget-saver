package com.project.expensemanger.manager.adaptor.in.api.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "시스템 헬스체크 API", description = "배포 및 상태 확인을 위한 헬스체크 API")
public interface HealthCheckControllerSpec {

    @Operation(
            summary = "헬스체크",
            description = "서비스 상태를 확인합니다."
    )
    ResponseEntity<String> healthz();
}
