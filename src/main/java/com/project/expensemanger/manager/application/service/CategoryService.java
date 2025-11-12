package com.project.expensemanger.manager.application.service;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterCategoryRequest;
import com.project.expensemanger.manager.application.port.in.CategoryUseCase;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import com.project.expensemanger.manager.domain.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {

    private final CategoryPort categoryPort;

    @Override
    public Long register(RegisterCategoryRequest requestDto) {
        validateDuplicateCategory(requestDto.name());
        Category category = Category.builder()
                .name(requestDto.name())
                .type(requestDto.categoryType())
                .build();

        Category savedCategory = categoryPort.save(category);
        return savedCategory.getId();
    }

    private void validateDuplicateCategory(String categoryName) {
        categoryPort.assertNameNotExists(categoryName);
    }
}
