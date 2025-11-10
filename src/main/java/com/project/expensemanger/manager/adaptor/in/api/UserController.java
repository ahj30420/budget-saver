package com.project.expensemanger.manager.adaptor.in.api;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.SignupRequest;
import com.project.expensemanger.manager.adaptor.in.api.spec.UserControllerSpec;
import com.project.expensemanger.manager.application.port.in.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerSpec {

    private final UserUseCase userUseCase;

    @Override
    @PostMapping("/api/signup")
    public void signup(@Valid @RequestBody SignupRequest requestDto) {
        userUseCase.registerUser(requestDto);
    }

}
