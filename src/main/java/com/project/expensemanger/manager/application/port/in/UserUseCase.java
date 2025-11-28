package com.project.expensemanger.manager.application.port.in;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.SignupRequest;

public interface UserUseCase {
    Long registerUser(SignupRequest request);
}
