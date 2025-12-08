package com.project.expensemanger.manager.adaptor.in.api;

import com.project.expensemanger.manager.adaptor.in.api.spec.HealthCheckControllerSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController implements HealthCheckControllerSpec {
    @GetMapping("/healthz")
    public ResponseEntity<String> healthz() {
        return ResponseEntity.ok().body("OK");
    }
}
