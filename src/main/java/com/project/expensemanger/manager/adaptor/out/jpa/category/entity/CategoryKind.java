package com.project.expensemanger.manager.adaptor.out.jpa.category.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryKind {
    CUSTOM("사용자 지정"),
    STANDARD("표준");

    private final String desc;
}
