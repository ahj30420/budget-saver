package com.project.expensemanger.manager.adaptor.out.jpa.category.entity;

import com.project.expensemanger.core.common.audting.BaseDateTime;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.entity.CategoryBudgetSummaryJpaEntity;
import com.project.expensemanger.manager.domain.category.Category;
import com.project.expensemanger.manager.domain.category.CategoryType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryJpaEntity extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryKind type;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "category")
    private CategoryBudgetSummaryJpaEntity categoryBudgetSummary;

    private boolean isDeleted = false;

    @Builder
    public CategoryJpaEntity(String name, CategoryKind type) {
        this.name = name;
        this.type = type;
    }

    public static CategoryJpaEntity from(Category category) {
        return CategoryJpaEntity.builder()
                .name(category.getName())
                .type(CategoryKind.valueOf(category.getType().name()))
                .build();
    }

    public Category toDomain() {
        return Category.builder()
                .id(this.getId())
                .name(this.getName())
                .type(CategoryType.valueOf(this.getType().name()))
                .build();
    }
}
