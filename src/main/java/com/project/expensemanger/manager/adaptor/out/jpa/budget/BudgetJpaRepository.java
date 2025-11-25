package com.project.expensemanger.manager.adaptor.out.jpa.budget;

import com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetSummaryDto;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.entity.BudgetJpaEntity;
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
                    select new com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetSummaryDto
                    (b.category.id, b.category.name, sum(b.amount))
                    from BudgetJpaEntity b
                    where b.category.type = com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryKind.STANDARD
                        and b.isDeleted = false
                    group by b.category.id     
            """)
    List<CategoryBudgetSummaryDto> findTotalBudgetByCategory();
}
