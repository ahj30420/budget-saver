package com.project.expensemanger.manager.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.BudgetErrorCode;
import com.project.expensemanger.core.common.exception.errorcode.CategoryErrorCode;
import com.project.expensemanger.manager.application.mock.BudgetMock;
import com.project.expensemanger.manager.application.mock.CategoryMock;
import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @InjectMocks
    private BudgetService sut;

    @Mock
    private BudgetPort budgetPort;

    @Mock
    private CategoryPort categoryPort;

    private BudgetMock budgetMock = new BudgetMock();

    private CategoryMock categoryMock = new CategoryMock();

    @Test
    @DisplayName("예산 등록 테스트 : 성공")
    void register_budget_list_success_test() throws Exception {
        // given
        given(categoryPort.findById(any(Long.class))).willReturn(categoryMock.standardDomainMock());
        given(budgetPort.saveAll(any(List.class))).willReturn(budgetMock.domainListMock());

        // when
        List<Long> budgetIdList = sut.registerBudget(any(Long.class), budgetMock.RegisterRequestDto());

        // then
        assertThat(budgetIdList).isEqualTo(budgetMock.domainIdListMock());
    }

    @Test
    @DisplayName("예산 등록 테스트 : 실패[예산이 이미 존재함]")
    void register_budget_list_fail_already_exit_test() throws Exception {
        // given
        given(categoryPort.findById(any(Long.class))).willReturn(categoryMock.standardDomainMock());

        doThrow(new BaseException(BudgetErrorCode.BUDGET_ALREADY_EXIST))
                .when(budgetPort)
                .assertDateAndUserIdAndCategoryNotExists(any(LocalDate.class), any(Long.class), any(Long.class));

        // when & then
        assertThrows(BaseException.class, () -> sut.registerBudget(any(Long.class), budgetMock.RegisterRequestDto()));
    }

    @Test
    @DisplayName("예산 등록 테스트 : 실패[카테고리가 존재하지 않음]")
    void register_budget_list_fail_not_exit_category_test() throws Exception {
        // given
        doThrow(new BaseException(CategoryErrorCode.CATEGORY_NOT_FOUND))
                .when(categoryPort)
                .findById(any(Long.class));

        // when & then
        assertThrows(BaseException.class, () -> sut.registerBudget(any(Long.class), budgetMock.RegisterRequestDto()));
    }

}