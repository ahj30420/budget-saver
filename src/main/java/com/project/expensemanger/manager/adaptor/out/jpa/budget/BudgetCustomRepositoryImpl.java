package com.project.expensemanger.manager.adaptor.out.jpa.budget;

import static com.project.expensemanger.manager.adaptor.out.jpa.budget.entity.QBudgetJpaEntity.budgetJpaEntity;
import static com.project.expensemanger.manager.adaptor.out.jpa.expenditure.entity.QExpenditureJpaEntity.expenditureJpaEntity;

import com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetUsageProjection;
import com.project.expensemanger.manager.application.service.dto.GetBudgetUsageCondition;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BudgetCustomRepositoryImpl implements BudgetCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CategoryBudgetUsageProjection> findTotalExpenditureByCategoryAndDateAndId(
            GetBudgetUsageCondition condition) {

        return queryFactory
                .select(Projections.constructor(
                        CategoryBudgetUsageProjection.class,
                        budgetJpaEntity.category.id,
                        budgetJpaEntity.category.name,
                        expenditureJpaEntity.amount.sum().coalesce(0L),
                        budgetJpaEntity.amount
                ))
                .from(budgetJpaEntity)
                .join(budgetJpaEntity.category)
                .leftJoin(expenditureJpaEntity)
                .on(
                        expenditureJpaEntity.category.id.eq(budgetJpaEntity.category.id)
                                .and(expenditureJpaEntity.spendAt.between(condition.startDate().atStartOfDay(), condition.endDate().atStartOfDay()))
                                .and(expenditureJpaEntity.user.id.eq(condition.userId()))
                                .and(expenditureJpaEntity.isDeleted.isFalse())
                )
                .where(
                        userIdEq(condition.userId()),
                        budgetDateBetween(condition.startDate(), condition.endDate()),
                        categoryIdIn(condition.categoryIdList())
                )
                .groupBy(budgetJpaEntity.category.id, budgetJpaEntity.category.name, budgetJpaEntity.amount)
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return budgetJpaEntity.user.id.eq(userId);
    }

    private BooleanExpression budgetDateBetween(LocalDate start, LocalDate end) {
        return budgetJpaEntity.date.between(start, end);
    }

    private BooleanExpression categoryIdIn(List<Long> categoryIdList) {
        if (ObjectUtils.isEmpty(categoryIdList)) {
            return null;
        }
        return budgetJpaEntity.category.id.in(categoryIdList);
    }
}
