package com.project.expensemanger.manager.adaptor.in.api.mapper;

import com.project.expensemanger.manager.adaptor.in.api.dto.response.CategoryIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.GetCategoryResponse;
import com.project.expensemanger.manager.domain.category.Category;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryIdResponse toIdDto(Long categoryId) {
        return new CategoryIdResponse(categoryId);
    }

    public GetCategoryResponse toGetDto(Category category) { return new GetCategoryResponse(category.getId(), category.getName()); }

    public List<GetCategoryResponse> toGetListDto(List<Category> allCategories) {
        return allCategories.stream().map(this::toGetDto).toList();
    }
}
