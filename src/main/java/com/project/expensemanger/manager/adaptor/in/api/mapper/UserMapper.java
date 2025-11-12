package com.project.expensemanger.manager.adaptor.in.api.mapper;

import com.project.expensemanger.manager.adaptor.in.api.dto.response.UserIdResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserIdResponse toIdDto(Long userId) {
        return new UserIdResponse(userId);
    }
}
