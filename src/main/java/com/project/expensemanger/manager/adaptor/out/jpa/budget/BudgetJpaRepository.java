package com.project.expensemanger.manager.adaptor.out.jpa.budget;

import com.project.expensemanger.manager.adaptor.out.jpa.budget.entity.BudgetJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetSummaryProjection;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetUsageProjection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BudgetJpaRepository extends JpaRepository<BudgetJpaEntity, Long> {

    @Query("""
                select b from BudgetJpaEntity b
                where b.user.id = :userId
                  and year(b.date) = year(:inputDate)
                  and month(b.date) = month(:inputDate)
                  and b.category.id = :categoryId
                  and b.isDeleted = false
            """)
    List<BudgetJpaEntity> existsByDateAndUserAndCategory(
            @Param("inputDate") LocalDate budgetDate,
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId
    );

    Optional<BudgetJpaEntity> findByIdAndUserIdAndIsDeletedFalse(Long budgetId, Long userId);

    List<BudgetJpaEntity> findByUserIdAndIsDeletedFalse(Long userId);

    @Query("""
                select b.category.id as categoryId,
                       b.category.name as categoryName,
                       sum(b.amount) as totalBudgetAmount
                from BudgetJpaEntity b
                where b.category.type = com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryKind.STANDARD
                  and b.isDeleted = false
                group by b.category.id
            """)
    List<CategoryBudgetSummaryProjection> findTotalBudgetByCategory();

    Optional<BudgetJpaEntity> findTopByUserIdAndIsDeletedFalseOrderByDateDesc(Long userId);


    @Query("""
                select 
                     c.id as categoryId,
                     c.name as categoryName,
                     COALESCE(sum(e.amount), 0) as expenditureAmount,
                     b.amount as budgetAmount
                from BudgetJpaEntity b
                join b.category c
                left join ExpenditureJpaEntity e 
                    on c.id = e.category.id
                    and e.spendAt between :startDate and :endDate
                    and e.user.id = :userId
                    and e.isDeleted = false
                where 
                    b.user.id = :userId
                    and b.date between :startDate and :endDate
                    and b.isDeleted = false
                group by c.id, c.name, b.amount
            """)
    List<CategoryBudgetUsageProjection> findTotalExpenditureByCategoryAndDateAndId(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("userId") Long userId);
}
