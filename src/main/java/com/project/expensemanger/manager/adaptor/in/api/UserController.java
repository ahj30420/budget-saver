package com.project.expensemanger.manager.adaptor.in.api;

import com.project.expensemanger.core.common.util.UrlCreator;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.SignupRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.UserIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.mapper.UserMapper;
import com.project.expensemanger.manager.adaptor.in.api.spec.UserControllerSpec;
import com.project.expensemanger.manager.application.port.in.UserUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerSpec {

    private static final String DEFAULT = "/api/users";
    private final UserUseCase userUseCase;
    private final UserMapper userMapper;

    @Override
    @PostMapping("/api/signup")
    public ResponseEntity<UserIdResponse> signup(@Valid @RequestBody SignupRequest requestDto) {
        Long userId = userUseCase.registerUser(requestDto);
        URI location = UrlCreator.createUri(DEFAULT, userId);
        return ResponseEntity.created(location).body(userMapper.toIdDto(userId));
    }

}
