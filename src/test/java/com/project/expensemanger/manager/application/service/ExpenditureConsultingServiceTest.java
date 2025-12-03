package com.project.expensemanger.manager.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.project.expensemanger.manager.application.mock.BudgetMock;
import com.project.expensemanger.manager.application.mock.CategoryBudgetUsageMock;
import com.project.expensemanger.manager.application.mock.ExpenditureMock;
import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.application.port.out.ConsultingPort;
import com.project.expensemanger.manager.application.port.out.ExpenditurePort;
import com.project.expensemanger.manager.application.service.dto.GetBudgetUsageCondition;
import com.project.expensemanger.manager.application.service.model.CategoryRecommendtaionModel;
import com.project.expensemanger.manager.application.service.model.TodayBudgetRecommendationModel;
import com.project.expensemanger.manager.application.service.model.TodayCategoryExpenditureModel;
import com.project.expensemanger.manager.application.service.model.TodayExpenditureSummaryModel;
import com.project.expensemanger.manager.domain.consulting.CategoryBudgetUsage;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenditureConsultingServiceTest {

    @InjectMocks
    private ExpenditureConsultingService sut;

    @Mock
    private BudgetPort budgetPort;

    @Mock
    private ConsultingPort consultingPort;

    @Mock
    private ExpenditurePort expenditurePort;

    private BudgetMock budgetMock = new BudgetMock();
    private CategoryBudgetUsageMock categoryBudgetUsageMock = new CategoryBudgetUsageMock();
    private ExpenditureMock expenditureMock = new ExpenditureMock();

    @Test
    @DisplayName("오늘 지출 추천 로직 테스트")
    void get_recommendation_test() throws Exception {
        // given
        Long userId = budgetMock.getUserId();
        List<CategoryBudgetUsage> categoryUsageList = List.of(categoryBudgetUsageMock.domainMock());

        given(budgetPort.findLastestBudget(anyLong())).willReturn(budgetMock.domainMock());
        given(consultingPort.getCategoryBudgetUsage(any(GetBudgetUsageCondition.class)))
                .willReturn(categoryUsageList);

        // when
        TodayBudgetRecommendationModel result = sut.getRecommendation(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.categories()).hasSize(1);
        assertThat(result.todayTotalPossible()).isGreaterThan(0);
        assertThat(result.message()).isNotEmpty();

        CategoryRecommendtaionModel categoryRecommendation = result.categories().get(0);
        assertThat(categoryRecommendation.categoryId()).isEqualTo(
                categoryBudgetUsageMock.domainMock().getCategoryId());
        assertThat(categoryRecommendation.categoryName()).isEqualTo(
                categoryBudgetUsageMock.domainMock().getCategoryName());
        assertThat(categoryRecommendation.recommendedAmount()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("오늘 지출 추천 로직 테스트 : 예산 없음/기간 벗어남")
    void get_recommendation_empty_test() throws Exception {
        // given
        Long userId = budgetMock.getUserId();

        given(budgetPort.findLastestBudget(anyLong())).willReturn(null);

        // when
        var result = sut.getRecommendation(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.categories()).isEmpty();
        assertThat(result.todayTotalPossible()).isEqualTo(0L);
        assertThat(result.message()).isEmpty();
    }

    @Test
    @DisplayName("오늘 지출 안내 로직 테스트")
    void get_today_expenditure_test() {
        // given
        Long userId = budgetMock.getUserId();

        List<Expenditure> todayExpenditures = List.of(expenditureMock.domainMock());
        List<CategoryBudgetUsage> categoryUsageList = List.of(categoryBudgetUsageMock.domainMock());

        given(budgetPort.findLastestBudget(anyLong())).willReturn(budgetMock.domainMock());
        given(expenditurePort.findTodayExpenditure(anyLong())).willReturn(todayExpenditures);
        given(consultingPort.getCategoryBudgetUsage(any())).willReturn(categoryUsageList);

        // when
        TodayExpenditureSummaryModel result = sut.getTodayExpenditure(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.totalTodayAmount()).isGreaterThan(0);
        assertThat(result.categories()).hasSize(1);

        TodayCategoryExpenditureModel categoryModel = result.categories().get(0);
        assertThat(categoryModel.categoryId()).isEqualTo(categoryBudgetUsageMock.domainMock().getCategoryId());
        assertThat(categoryModel.categoryName()).isEqualTo(categoryBudgetUsageMock.domainMock().getCategoryName());
        assertThat(categoryModel.todayAmount()).isGreaterThanOrEqualTo(0);
        assertThat(categoryModel.todayRecommendedAmount()).isGreaterThanOrEqualTo(0);
        assertThat(categoryModel.riskPercentage()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("오늘 지출 안내 로직 테스트 : 예산 없음 또는 지출 데이터 없음")
    void get_today_expenditure_empty_test() {
        // given
        Long userId = budgetMock.getUserId();

        given(budgetPort.findLastestBudget(anyLong())).willReturn(null);

        // when
        TodayExpenditureSummaryModel result = sut.getTodayExpenditure(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.totalTodayAmount()).isEqualTo(0L);
        assertThat(result.categories()).isEmpty();
    }
}