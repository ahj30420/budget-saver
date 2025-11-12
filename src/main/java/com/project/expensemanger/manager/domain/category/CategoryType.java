package com.project.expensemanger.manager.domain.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
    CUSTOM("사용자 지정"),
    STANDARD("표준");

    private final String desc;
}
