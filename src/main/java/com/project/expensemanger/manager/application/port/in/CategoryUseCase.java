package com.project.expensemanger.manager.application.port.in;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterCategoryRequest;
import com.project.expensemanger.manager.domain.category.Category;
import java.util.List;

public interface CategoryUseCase {
    Long register(RegisterCategoryRequest requestDto);

    Category getCategory(Long categoryId);

    List<Category> getAllCategories();
}
