package com.project.expensemanger.manager.application.port.out;

import com.project.expensemanger.manager.domain.category.Category;
import java.util.List;

public interface CategoryPort {
    Category save(Category category);

    void assertNameNotExists(String categoryName);

    Category findById(Long categoryId);

    List<Category> findAllByType();
}
