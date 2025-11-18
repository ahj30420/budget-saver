package com.project.expensemanger.manager.adaptor.out.jpa.budget.entity;

import com.project.expensemanger.core.common.audting.BaseDateTime;
import com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryJpaEntity;
import jakarta.persistence.Entity;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category_budget_summary")
public class CategoryBudgetSummaryJpaEntity extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "category_id")
    private CategoryJpaEntity category;

    private Long totalAmount;

    @Builder
    public CategoryBudgetSummaryJpaEntity(Long id, CategoryJpaEntity category, Long totalAmount) {
        this.id = id;
        this.category = category;
        this.totalAmount = totalAmount;
    }
}
