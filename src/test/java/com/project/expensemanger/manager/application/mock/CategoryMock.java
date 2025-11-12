package com.project.expensemanger.manager.application.mock;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterCategoryRequest;
import com.project.expensemanger.manager.domain.category.Category;
import com.project.expensemanger.manager.domain.category.CategoryType;
import org.springframework.stereotype.Component;

@Component
public class CategoryMock {

    private final Long id = 1L;
    private final String name = "식비";

    public Category standardDomainMock() {
        return Category.builder()
                .id(id)
                .name(name)
                .type(CategoryType.STANDARD)
                .build();
    }

    public RegisterCategoryRequest standardRegisterRequestDto() {
        return new RegisterCategoryRequest(name, null);
    }

}
