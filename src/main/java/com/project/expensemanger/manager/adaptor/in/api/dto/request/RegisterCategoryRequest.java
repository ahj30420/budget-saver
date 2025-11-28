package com.project.expensemanger.manager.adaptor.in.api.dto.request;

import com.project.expensemanger.manager.domain.category.CategoryType;
import jakarta.validation.constraints.NotNull;

public record RegisterCategoryRequest(
        @NotNull
        String name,

        CategoryType categoryType
) {
    public RegisterCategoryRequest {
        categoryType = CategoryType.STANDARD;
    }
}
