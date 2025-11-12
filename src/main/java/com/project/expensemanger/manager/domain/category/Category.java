package com.project.expensemanger.manager.domain.category;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Category {
    private Long id;
    private String name;
    private CategoryType type;

    @Builder
    public Category(Long id, String name, CategoryType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
