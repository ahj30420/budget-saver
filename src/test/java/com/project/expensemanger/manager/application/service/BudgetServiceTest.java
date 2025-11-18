package com.project.expensemanger.manager.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.BudgetErrorCode;
import com.project.expensemanger.core.common.exception.errorcode.CategoryErrorCode;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateBudgetRequest;
import com.project.expensemanger.manager.application.mock.BudgetMock;
import com.project.expensemanger.manager.application.mock.CategoryMock;
import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import com.project.expensemanger.manager.domain.budget.Budget;
import com.project.expensemanger.manager.domain.budget.recommendation.BudgetRecommendationContext;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.RecommendedBudgetResult;
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

    @Mock
    private BudgetRecommendationContext context;

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
        assertThatThrownBy(() -> sut.registerBudget(any(Long.class), budgetMock.RegisterRequestDto()))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.BUDGET_ALREADY_EXIST.getMessage());
    }

    @Test
    @DisplayName("예산 등록 테스트 : 실패[카테고리가 존재하지 않음]")
    void register_budget_list_fail_not_exit_category_test() throws Exception {
        // given
        doThrow(new BaseException(CategoryErrorCode.CATEGORY_NOT_FOUND))
                .when(categoryPort)
                .findById(any(Long.class));

        // when & then
        assertThatThrownBy(() -> sut.registerBudget(any(Long.class), budgetMock.RegisterRequestDto()))
                .isInstanceOf(BaseException.class)
                .hasMessage(CategoryErrorCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("예산 단건 조회 테스트 : 성공")
    void get_budget_success_test() throws Exception {
        // given
        Budget budget = budgetMock.domainMock();
        given(budgetPort.findByIdAndUserId(any(Long.class), any(Long.class)))
                .willReturn(budget);

        // when
        Budget result = sut.getBudget(budget.getId(), budget.getUserId());

        // then
        assertThat(result).isEqualTo(budget);
    }

    @Test
    @DisplayName("예산 단건 조회 테스트 : 실패[해당 예산 조회 결과 없음]")
    void get_budget_failure_test() throws Exception {
        // given
        doThrow(new BaseException(BudgetErrorCode.BUDGET_NOT_FOUND))
                .when(budgetPort)
                .findByIdAndUserId(any(Long.class), any(Long.class));

        // when & then
        assertThatThrownBy(() -> sut.getBudget(any(Long.class), any(Long.class)))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.BUDGET_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("사용자 예산 목록 조회 테스트 : 성공")
    void get_budget_list_success_test() throws Exception {
        // given
        List<Budget> budgets = budgetMock.domainListMock();
        given(sut.getBudgetList(any(Long.class))).willReturn(budgets);

        // when
        List<Budget> result = sut.getBudgetList(budgetMock.getId());

        // then
        assertThat(budgets).isEqualTo(result);
    }

    @Test
    @DisplayName("예산 수정 테스트 : 성공")
    void update_budget_success_test() throws Exception {
        // given
        UpdateBudgetRequest requestDto = budgetMock.UpdateRequestDto();
        Budget budget = budgetMock.domainMock();
        given(budgetPort.findByIdAndUserId(any(Long.class), any(Long.class))).willReturn(budget);

        // when
        Budget changedBudget = sut.updateBudget(budget.getUserId(), budget.getCategoryId(), requestDto);

        // then
        assertThat(changedBudget.getId()).isEqualTo(budget.getId());
        assertThat(changedBudget.getAmount()).isEqualTo(requestDto.amount());
        assertThat(changedBudget.getCategoryId()).isEqualTo(budget.getCategoryId());
    }

    @Test
    @DisplayName("예산 수정 테스트 : 실패[수정할 예산을 찾지 못한 경우]")
    void update_budget_fail_when_not_find_budget() throws Exception {
        // given
        doThrow(new BaseException(BudgetErrorCode.BUDGET_NOT_FOUND))
                .when(budgetPort)
                .findByIdAndUserId(any(Long.class), any(Long.class));

        // when & then
        assertThatThrownBy(() -> sut.updateBudget(1L, 1L, budgetMock.UpdateRequestDto()))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.BUDGET_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("예산 수정 테스트 : 실패[도메인 예외 발생 시]")
    void update_budget_fail_when_domain_execption() throws Exception {
        // given
        Budget budget = budgetMock.domainMock();
        given(budgetPort.findByIdAndUserId(any(Long.class), any(Long.class))).willReturn(budget);

        // when & then
        assertThatThrownBy(() -> sut.updateBudget(1L, 1L, budgetMock.ZeroAmountUpdateRequestDto()))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.INVALID_BUDGET_AMOUNT.getMessage());

    }

    @Test
    @DisplayName("예산 삭제 테스트 : 성공")
    void delete_budget_success_test() throws Exception {
        // given
        Budget budget = budgetMock.domainMock();
        given(budgetPort.findByIdAndUserId(any(Long.class), any(Long.class))).willReturn(budget);
        willDoNothing().given(budgetPort).delete(any(Budget.class));

        // when
        sut.deleteBudget(budget.getId(), budget.getUserId());

        // then
        verify(budgetPort, times(1)).delete(any(Budget.class));
    }

    @Test
    @DisplayName("예산 삭제 테스트 : 실패[해당 예산을 찾지 못했을 경우]")
    void delete_budget_fail_when_not_found_budget() throws Exception {
        // given
        doThrow(new BaseException(BudgetErrorCode.BUDGET_NOT_FOUND))
                .when(budgetPort)
                .findByIdAndUserId(any(Long.class), any(Long.class));

        // when & then
        assertThatThrownBy(() -> sut.deleteBudget(1L, 1L))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.BUDGET_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("예산 추천 테스트 : 성공")
    void get_recommend_budget_success_test() throws Exception {
        // given
        List<RecommendedBudgetResult> recommendedBudgetResults = budgetMock.recommendedBudgetListMock();
        given(budgetPort.findTotalBudgetByCategory()).willReturn(budgetMock.categoryBudgetStatMock());
        given(context.recommend(anyLong(), anyList())).willReturn(recommendedBudgetResults);

        // when
        List<RecommendedBudgetResult> result = sut.getRecommendBudgetByCategory(5000L);

        // then
        assertThat(result).isEqualTo(recommendedBudgetResults);
    }

    @Test
    @DisplayName("예산 추천 테스트 : 실패[예산 추천 전략 조회 실패]")
    void get_recommend_budget_fail_when_not_found_recommend_strategy() throws Exception {
        // given
        given(budgetPort.findTotalBudgetByCategory()).willReturn(budgetMock.categoryBudgetStatMock());
        doThrow(new BaseException(BudgetErrorCode.NOT_FOUND_RECOMMEND_STRATEGY))
                .when(context).recommend(anyLong(), anyList());

        // when & then
        assertThatThrownBy(() -> sut.getRecommendBudgetByCategory(5000L))
                .isInstanceOf(BaseException.class)
                .hasMessage(BudgetErrorCode.NOT_FOUND_RECOMMEND_STRATEGY.getMessage());
    }
}