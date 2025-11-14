package com.project.expensemanger.manager.adaptor.out;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.BudgetErrorCode;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.BudgetJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.entity.BudgetJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.category.CategoryJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.user.UserJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.user.entity.UserJpaEntity;
import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.domain.budget.Budget;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BudgetPersistenceAdapter implements BudgetPort {

    private final BudgetJpaRepository budgetJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<Budget> saveAll(List<Budget> budgetList) {
        List<BudgetJpaEntity> entities = budgetList.stream()
                .map(b -> {
                    UserJpaEntity user = userJpaRepository.getReferenceById(b.getUserId());
                    CategoryJpaEntity category = categoryJpaRepository.getReferenceById(b.getCategoryId());
                    return BudgetJpaEntity.from(b, user, category);
                }).toList();

        List<BudgetJpaEntity> saved = budgetJpaRepository.saveAll(entities);

        return saved.stream().map(BudgetJpaEntity::toDmain).toList();
    }

    @Override
    public void assertDateAndUserIdAndCategoryNotExists(LocalDate budgetDate, Long categoryId, Long userId) {
        List<BudgetJpaEntity> existingBudgets =
                budgetJpaRepository.existsByDateAndUserAndCategory(budgetDate, userId, categoryId);

        if (!existingBudgets.isEmpty()) {
            throw new BaseException(BudgetErrorCode.BUDGET_ALREADY_EXIST);
        }
    }

    @Override
    public Budget findByIdAndUserId(Long budgetId, Long userId) {
        return budgetJpaRepository.findByIdAndUserIdAndIsDeletedFalse(budgetId, userId)
                .map(BudgetJpaEntity::toDmain)
                .orElseThrow(() -> new BaseException(BudgetErrorCode.BUDGET_NOT_FOUND));
    }
}
