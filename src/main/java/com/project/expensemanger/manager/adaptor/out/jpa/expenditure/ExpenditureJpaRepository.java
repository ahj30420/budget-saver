package com.project.expensemanger.manager.adaptor.out.jpa.expenditure;

import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.projection.ExpenditureDetailProjection;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.entity.ExpenditureJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenditureJpaRepository extends JpaRepository<ExpenditureJpaEntity, Long> {
    Optional<ExpenditureJpaEntity> findByIdAndIsDeletedFalse(Long expenditureId);

    @Query("""
                select 
                    e.id as expenditureId,
                    u.id as userId,
                    e.spendAt as spendAt,
                    e.amount as amount,
                    e.memo as memo,
                    c.id as categoryId,
                    c.name as categoryName
                from ExpenditureJpaEntity e
                join e.user u
                join e.category c
                where e.id = :expenditureId and e.isDeleted = false
            """)
    Optional<ExpenditureDetailProjection> findDetailById(Long expenditureId);
}
