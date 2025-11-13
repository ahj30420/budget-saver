package com.project.expensemanger.manager.adaptor.out.jpa.budget.entity;

import com.project.expensemanger.core.common.audting.BaseDateTime;
import com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.user.entity.UserJpaEntity;
import com.project.expensemanger.manager.domain.budget.Budget;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "budget")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetJpaEntity extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private Long amount;

    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryJpaEntity category;

    @Builder
    public BudgetJpaEntity(Long id, LocalDate date, Long amount, UserJpaEntity user, CategoryJpaEntity category) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.user = user;
        this.category = category;
    }

    public static BudgetJpaEntity from(Budget budget, UserJpaEntity userJpa, CategoryJpaEntity categoryJpa) {
        return BudgetJpaEntity.builder()
                .id(budget.getId())
                .date(budget.getDate())
                .amount(budget.getAmount())
                .user(userJpa)
                .category(categoryJpa)
                .build();
    }

    public Budget toDmain() {
        return Budget.builder()
                .id(id)
                .date(date)
                .amount(amount)
                .categoryId(category.getId())
                .userId(user.getId())
                .build();
    }
}
