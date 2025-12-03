package com.project.expensemanger.manager.adaptor.out;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.BudgetErrorCode;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.BudgetJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.CategoryBudgetSummaryJpaRespository;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.entity.BudgetJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.category.CategoryJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.user.UserJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.user.entity.UserJpaEntity;
import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.domain.budget.Budget;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.CategoryBudgetStat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BudgetPersistenceAdapter implements BudgetPort {

    private final BudgetJpaRepository budgetJpaRepository;
    private final CategoryBudgetSummaryJpaRespository categoryBudgetSummaryJpaRespository;
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
        return findByIdAndUserIdAndIsDeletedFalse(budgetId, userId).toDmain();
    }

    @Override
    public List<Budget> findByUserId(Long userId) {
        return budgetJpaRepository.findByUserIdAndIsDeletedFalse(userId).stream()
                .map(BudgetJpaEntity::toDmain)
                .toList();
    }

    @Override
    public void update(Budget updateBudget) {
        BudgetJpaEntity entity = findByIdAndUserIdAndIsDeletedFalse(updateBudget.getId(), updateBudget.getUserId());
        CategoryJpaEntity category = categoryJpaRepository.getReferenceById(updateBudget.getCategoryId());
        entity.updateFromDomain(updateBudget, category);
    }

    @Override
    public void delete(Budget budget) {
        BudgetJpaEntity entity = findByIdAndUserIdAndIsDeletedFalse(budget.getId(), budget.getUserId());
        entity.delete();

    }

    private BudgetJpaEntity findByIdAndUserIdAndIsDeletedFalse(Long budgetId, Long userId) {
        return budgetJpaRepository.findByIdAndUserIdAndIsDeletedFalse(budgetId, userId)
                .orElseThrow(() -> new BaseException(BudgetErrorCode.BUDGET_NOT_FOUND));
    }

    @Override
    public List<CategoryBudgetStat> findTotalBudgetByCategory() {
        return budgetJpaRepository.findTotalBudgetByCategory().stream()
                .map(row -> new CategoryBudgetStat(
                        row.getCategoryId(),
                        row.getCategoryName(),
                        row.getTotalBudgetAmount()
                )).toList();
    }

    @Override
    public List<CategoryBudgetStat> findSummaryByCategory() {
        return categoryBudgetSummaryJpaRespository.findSummaryByCategory().stream()
                .map(row -> new CategoryBudgetStat(
                        row.getCategoryId(),
                        row.getCategoryName(),
                        row.getTotalBudgetAmount()
                )).toList();
    }

    @Override
    public Budget findLastestBudget(Long userId) {
        return budgetJpaRepository.findTopByUserIdAndIsDeletedFalseOrderByDateDesc(userId)
                .map(BudgetJpaEntity::toDmain)
                .orElse(null);
    }
}
