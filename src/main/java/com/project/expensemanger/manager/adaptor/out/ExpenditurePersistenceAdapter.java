package com.project.expensemanger.manager.adaptor.out;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.ExpenditureErrorCode;
import com.project.expensemanger.manager.adaptor.out.jpa.category.CategoryJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.category.entity.CategoryJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.ExpenditureJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.dto.GetExpenditureListCondition;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.entity.ExpenditureJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.projection.ExpenditureDetailProjection;
import com.project.expensemanger.manager.adaptor.out.jpa.user.UserJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.user.entity.UserJpaEntity;
import com.project.expensemanger.manager.application.service.model.ExpenditureByCategoryModel;
import com.project.expensemanger.manager.application.service.model.ExpenditureDetailModel;
import com.project.expensemanger.manager.application.port.out.ExpenditurePort;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExpenditurePersistenceAdapter implements ExpenditurePort {

    private final ExpenditureJpaRepository expenditureJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Expenditure save(Expenditure expenditure) {
        UserJpaEntity user = userJpaRepository.getReferenceById(expenditure.getUserId());
        CategoryJpaEntity category = categoryJpaRepository.getReferenceById(expenditure.getCategoryId());

        ExpenditureJpaEntity saved = expenditureJpaRepository.save(
                ExpenditureJpaEntity.from(expenditure, user, category));
        return saved.toDomain();
    }

    @Override
    public Expenditure findById(Long expenditureId) {
        return findByIdAndIsDeletedFalse(expenditureId).toDomain();
    }

    @Override
    public void delete(Expenditure expenditure) {
        ExpenditureJpaEntity entity = findByIdAndIsDeletedFalse(expenditure.getId());
        entity.delete();
    }

    private ExpenditureJpaEntity findByIdAndIsDeletedFalse(Long expenditureId) {
        return expenditureJpaRepository.findByIdAndIsDeletedFalse(expenditureId)
                .orElseThrow(() -> new BaseException(ExpenditureErrorCode.EXPENDITURE_NOT_FOUND));
    }

    @Override
    public void update(Expenditure updateExpenditure) {
        ExpenditureJpaEntity entity = findByIdAndIsDeletedFalse(updateExpenditure.getId());
        CategoryJpaEntity category = categoryJpaRepository.getReferenceById(updateExpenditure.getCategoryId());
        entity.updateFromDomain(updateExpenditure, category);
    }

    @Override
    public ExpenditureDetailModel getDetails(Long expenditureId) {
        ExpenditureDetailProjection p = expenditureJpaRepository.findDetailById(expenditureId)
                .orElseThrow(() -> new BaseException(ExpenditureErrorCode.EXPENDITURE_NOT_FOUND));

        return new ExpenditureDetailModel(
                p.getExpenditureId(),
                p.getUserId(),
                p.getSpendAt(),
                p.getAmount(),
                p.getMemo(),
                p.getCategoryId(),
                p.getCategoryName()
        );
    }

    @Override
    public List<Expenditure> findAllExpenditureByCondition(GetExpenditureListCondition conditionDto) {
        return expenditureJpaRepository.findAllExpenditureByCondition(conditionDto)
                .stream()
                .map(ExpenditureJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenditureByCategoryModel> findTotalExpenditureByCategory(GetExpenditureListCondition conditionDto) {
        return expenditureJpaRepository.findTotalExpenditureByCategory(conditionDto)
                .stream()
                .map(entity -> {
                    return new ExpenditureByCategoryModel(
                            entity.categoryId(),
                            entity.categoryName(),
                            entity.totalAmount());
                }).collect(Collectors.toList());
    }

    @Override
    public List<Expenditure> findTodayExpenditure(Long userId) {
        return expenditureJpaRepository.findTodayExpenditures(userId)
                .stream()
                .map(ExpenditureJpaEntity::toDomain)
                .collect(Collectors.toList());
    }
}
