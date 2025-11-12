package com.project.expensemanger.manager.adaptor.in.api.mapper;

import com.project.expensemanger.manager.adaptor.in.api.dto.response.CategoryIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.GetCategoryResponse;
import com.project.expensemanger.manager.domain.category.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryIdResponse toIdDto(Long categoryId) {
        return new CategoryIdResponse(categoryId);
    }

    public GetCategoryResponse toGetDto(Category category) { return new GetCategoryResponse(category.getId(), category.getName()); }
}
