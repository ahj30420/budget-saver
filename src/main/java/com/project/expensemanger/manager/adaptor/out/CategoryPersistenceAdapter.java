package com.project.expensemanger.manager.adaptor.out;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.CategoryErrorCode;
import com.project.expensemanger.manager.adaptor.out.jpa.category.CategoryJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryJpaEntity;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import com.project.expensemanger.manager.domain.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPort {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category save(Category category) {
        CategoryJpaEntity saved = categoryJpaRepository.save(CategoryJpaEntity.from(category));
        return saved.toDomain();
    }

    @Override
    public void assertNameNotExists(String categoryName) {
        categoryJpaRepository.findByNameAndIsDeletedFalse(categoryName)
                .ifPresent(category -> {
                    throw new BaseException(CategoryErrorCode.CATEGORY_ALREADY_EXIST);
                });
    }
}
