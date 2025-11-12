package com.project.expensemanger.manager.application.mock;

import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterCategoryRequest;
import com.project.expensemanger.manager.domain.category.Category;
import com.project.expensemanger.manager.domain.category.CategoryType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CategoryMock {

    private final Long id = 1L;
    private final String name = "식비";
    private final List<String> nameList = List.of("식비", "교통비", "통신비", "관리비", "월세");

    public Category standardDomainMock() {
        return Category.builder()
                .id(id)
                .name(name)
                .type(CategoryType.STANDARD)
                .build();
    }

    public List<Category> standardDomainListMock() {
        List<Category> result = new ArrayList<>();
        for (int i = 0; i < nameList.size(); i++) {
            Category category = Category.builder()
                    .id((long)i+1)
                    .name(nameList.get(i))
                    .type(CategoryType.STANDARD)
                    .build();
            result.add(category);
        }
        return result;
    }

    public RegisterCategoryRequest standardRegisterRequestDto() {
        return new RegisterCategoryRequest(name, null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
