package com.project.expensemanger.manager.adaptor.out.jpa.expenditure;

import static com.project.expensemanger.manager.adaptor.out.jpa.expenditure.entity.QExpenditureJpaEntity.expenditureJpaEntity;
import static org.springframework.util.ObjectUtils.isEmpty;

import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.dto.GetExpenditureListCondition;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.entity.ExpenditureJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.projection.ExpenditureByCategoryProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExpenditureCustomRepositoryImpl implements ExpenditureCustomRepository  {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ExpenditureJpaEntity> findAllExpenditureByCondition(GetExpenditureListCondition condition) {
        return queryFactory
                .selectFrom(expenditureJpaEntity)
                .join(expenditureJpaEntity.category)
                .where(
                        expenditureJpaEntity.isDeleted.eq(false),
                        userIdCheck(condition.userId()),
                        categoryIdCheck(condition.categoryId()),
                        spendAtRange(condition.startDate(), condition.endDate()),
                        amountRange(condition.minAmount(), condition.maxAmount())
                ).fetch();
    }

    @Override
    public List<ExpenditureByCategoryProjection> findTotalExpenditureByCategory(
            GetExpenditureListCondition conditionDto) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ExpenditureByCategoryProjection.class,
                                expenditureJpaEntity.category.id.as("categoryId"),
                                expenditureJpaEntity.category.name.as("categoryName"),
                                expenditureJpaEntity.amount.sum().as("totalAmount")))
                .from(expenditureJpaEntity)
                .where(
                        expenditureJpaEntity.isDeleted.eq(false),
                        excludedFromTotalCheck(),
                        userIdCheck(conditionDto.userId()),
                        categoryIdCheck(conditionDto.categoryId()),
                        spendAtRange(conditionDto.startDate(), conditionDto.endDate()),
                        amountRange(conditionDto.minAmount(), conditionDto.maxAmount())
                )
                .groupBy(expenditureJpaEntity.category.id)
                .fetch();
    }

    private BooleanExpression userIdCheck(Long userId) {
        return expenditureJpaEntity.user.id.eq(userId);
    }

    private BooleanExpression categoryIdCheck(Long categoryId) {
        return isEmpty(categoryId) ? null : expenditureJpaEntity.category.id.eq(categoryId);
    }

    private BooleanExpression spendAtRange(LocalDateTime startDate, LocalDateTime endDate) {
        return expenditureJpaEntity.spendAt.between(startDate, endDate);
    }

    private BooleanExpression amountRange(Long minAmount, Long maxAmount) {
        if (isEmpty(minAmount) && isEmpty(maxAmount)) {
            return null;
        }
        return expenditureJpaEntity.amount.between(minAmount, maxAmount);
    }

    private BooleanExpression excludedFromTotalCheck() {
        return expenditureJpaEntity.excludedFromTotal.eq(false);
    }
}
