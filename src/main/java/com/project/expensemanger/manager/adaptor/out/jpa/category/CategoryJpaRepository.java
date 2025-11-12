package com.project.expensemanger.manager.adaptor.out.jpa.category;

import com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {
    Optional<CategoryJpaEntity> findByNameAndIsDeletedFalse(String name);
}
