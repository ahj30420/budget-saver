package com.project.expensemanger.manager.adaptor.out.jpa.budget;

import com.project.expensemanger.manager.adaptor.out.jpa.budget.entity.CategoryBudgetSummaryJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetSummaryProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryBudgetSummaryJpaRespository extends JpaRepository<CategoryBudgetSummaryJpaEntity, Long> {
    @Query("""
            select cb.category.id as categoryId,
                   cb.category.name as categoryName,
                   cb.totalAmount as totalBudgetAmount
            from CategoryBudgetSummaryJpaEntity cb
            """)
    List<CategoryBudgetSummaryProjection> findSummaryByCategory();
}
