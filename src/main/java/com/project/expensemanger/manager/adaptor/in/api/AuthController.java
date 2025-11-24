package com.project.expensemanger.manager.adaptor.in.api;

import com.project.expensemanger.manager.adaptor.in.api.spec.AuthControllerSpec;
import com.project.expensemanger.manager.application.port.in.AuthUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerSpec {

    private final AuthUseCase authUseCase;

    @Override
    @PostMapping("/api/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        authUseCase.reissue(request, response);
        return ResponseEntity.noContent().build();
    }

}
