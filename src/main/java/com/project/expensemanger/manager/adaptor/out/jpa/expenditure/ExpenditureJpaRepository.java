package com.project.expensemanger.manager.adaptor.out.jpa.expenditure;

import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.entity.ExpenditureJpaEntity;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenditureJpaRepository extends JpaRepository<ExpenditureJpaEntity, Long> {
    Optional<ExpenditureJpaEntity> findByIdAndIsDeletedFalse(Long expenditureId);
}
