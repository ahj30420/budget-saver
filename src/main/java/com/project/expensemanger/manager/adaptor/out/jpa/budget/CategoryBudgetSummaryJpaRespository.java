package com.project.expensemanger.manager.adaptor.out.jpa.budget;

import com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetSummaryDto;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.entity.CategoryBudgetSummaryJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryBudgetSummaryJpaRespository extends JpaRepository<CategoryBudgetSummaryJpaEntity, Long> {
    @Query("""
                    select new com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetSummaryDto
                    (cb.category.id, cb.category.name, cb.totalAmount)
                    from CategoryBudgetSummaryJpaEntity cb     
            """)
    List<CategoryBudgetSummaryDto> findSummaryByCategory();
}
