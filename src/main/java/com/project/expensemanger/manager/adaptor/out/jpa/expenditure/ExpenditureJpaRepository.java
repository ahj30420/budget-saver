package com.project.expensemanger.manager.adaptor.out.jpa.expenditure;

import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.entity.ExpenditureJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenditureJpaRepository extends JpaRepository<ExpenditureJpaEntity, Long> {
}
