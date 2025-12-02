package com.project.expensemanger.manager.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.project.expensemanger.manager.application.mock.BudgetMock;
import com.project.expensemanger.manager.application.mock.CategoryBudgetUsageMock;
import com.project.expensemanger.manager.application.port.out.BudgetPort;
import com.project.expensemanger.manager.application.port.out.ConsultingPort;
import com.project.expensemanger.manager.application.service.dto.GetBudgetUsageCondition;
import com.project.expensemanger.manager.application.service.model.CategoryRecommendtaionModel;
import com.project.expensemanger.manager.application.service.model.TodayBudgetRecommendationModel;
import com.project.expensemanger.manager.domain.consulting.CategoryBudgetUsage;
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

    private BudgetMock budgetMock = new BudgetMock();
    private CategoryBudgetUsageMock categoryBudgetUsageMock = new CategoryBudgetUsageMock();

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
        assertThat(result.getCategories()).hasSize(1);
        assertThat(result.getTodayTotalPossible()).isGreaterThan(0);
        assertThat(result.getMessage()).isNotEmpty();

        CategoryRecommendtaionModel categoryRecommendation = result.getCategories().get(0);
        assertThat(categoryRecommendation.getCategoryId()).isEqualTo(
                categoryBudgetUsageMock.domainMock().getCategoryId());
        assertThat(categoryRecommendation.getCategoryName()).isEqualTo(
                categoryBudgetUsageMock.domainMock().getCategoryName());
        assertThat(categoryRecommendation.getRecommendedAmount()).isGreaterThan(0L);
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
        assertThat(result.getCategories()).isEmpty();
        assertThat(result.getTodayTotalPossible()).isEqualTo(0L);
        assertThat(result.getMessage()).isEmpty();
    }
}