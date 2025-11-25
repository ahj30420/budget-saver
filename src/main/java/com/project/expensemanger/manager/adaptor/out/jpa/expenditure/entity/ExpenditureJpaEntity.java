package com.project.expensemanger.manager.adaptor.out.jpa.expenditure.entity;

import com.project.expensemanger.core.common.audting.BaseDateTime;
import com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.user.entity.UserJpaEntity;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "expenditure")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpenditureJpaEntity extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime spendAt;
    private Long amount;
    private String memo;

    private boolean excludedFromTotal;
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryJpaEntity category;


    @Builder
    public ExpenditureJpaEntity(LocalDateTime spendAt, Long amount, String memo, boolean excludedFromTotal,
                                UserJpaEntity user, CategoryJpaEntity category) {
        this.spendAt = spendAt;
        this.amount = amount;
        this.memo = memo;
        this.excludedFromTotal = excludedFromTotal;
        this.user = user;
        this.category = category;
    }

    public static ExpenditureJpaEntity from(Expenditure expenditure, UserJpaEntity user, CategoryJpaEntity category) {
        return ExpenditureJpaEntity.builder()
                .spendAt(expenditure.getSpentAt())
                .amount(expenditure.getAmount())
                .memo(expenditure.getMemo())
                .excludedFromTotal(expenditure.isExcludedFromTotal())
                .user(user)
                .category(category)
                .build();
    }

    public Expenditure toDomain() {
        return Expenditure.builder()
                .id(this.id)
                .userId(this.user.getId())
                .categoryId(this.category.getId())
                .spentAt(spendAt)
                .amount(amount)
                .memo(memo)
                .excludedFromTotal(excludedFromTotal)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
