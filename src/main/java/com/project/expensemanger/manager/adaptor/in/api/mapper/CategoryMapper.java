package com.project.expensemanger.manager.adaptor.in.api.mapper;

import com.project.expensemanger.manager.adaptor.in.api.dto.response.CategoryIdResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryIdResponse toIdDto(Long categoryId) {
        return new CategoryIdResponse(categoryId);
    }
}
