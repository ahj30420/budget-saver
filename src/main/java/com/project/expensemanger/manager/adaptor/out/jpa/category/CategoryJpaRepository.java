package com.project.expensemanger.manager.adaptor.out.jpa.category;

import com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {
    Optional<CategoryJpaEntity> findByNameAndIsDeletedFalse(String name);

    Optional<CategoryJpaEntity> findByIdAndIsDeletedFalse(Long categoryId);

    @Query(
            "select c from CategoryJpaEntity c "
                    + "where c.type = com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryKind.STANDARD"
                    + " and c.isDeleted = false"
    )
    List<CategoryJpaEntity> findAllByType();
}
