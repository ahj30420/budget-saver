package com.project.expensemanger.manager.adaptor.in.api;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.core.common.security.annotation.MockCustomUser;
import com.project.expensemanger.core.common.security.config.AuthTestConfig;
import com.project.expensemanger.core.config.JacksonConfig;
import com.project.expensemanger.core.config.SecurityConfig;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateBudgetRequest;
import com.project.expensemanger.manager.adaptor.in.api.mapper.BudgetMapper;
import com.project.expensemanger.manager.application.mock.BudgetMock;
import com.project.expensemanger.manager.application.port.in.BudgetUseCase;
import com.project.expensemanger.manager.domain.budget.Budget;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(BudgetController.class)
@Import({SecurityConfig.class, AuthTestConfig.class, BudgetUseCase.class, BudgetMapper.class, BudgetMock.class})
@AutoConfigureMockMvc
class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BudgetMock budgetMock;

    @MockitoBean
    private BudgetUseCase budgetUseCase;

    @Test
    @DisplayName("예산 등록 테스트 : 성공")
    @MockCustomUser
    void register_budget_list_success_test() throws Exception {
        // given
        RegisterBudgetList requestDto = budgetMock.RegisterRequestDto();
        String content = objectMapper.writeValueAsString(requestDto);

        given(budgetUseCase.registerBudget(any(Long.class), any(RegisterBudgetList.class))).willReturn(budgetMock.domainIdListMock());

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/budget/list")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.length()").value(budgetMock.domainIdListMock().size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }

    @Test
    @DisplayName("예산 단건 조회 테스트")
    @MockCustomUser
    void get_budget_success_test() throws Exception {
        // given
        Budget budget = budgetMock.domainMock();
        given(budgetUseCase.getBudget(anyLong(), anyLong())).willReturn(budget);

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/budget" + "/{budgetId}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(budget.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.categoryId").value(budget.getCategoryId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.amount").value(budget.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.date").value(budget.getDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }

    @Test
    @DisplayName("사용자 예산 목록 조회 테스트 : 성공")
    @MockCustomUser
    void get_budget_list_success_test() throws Exception {
        // given
        List<Budget> budgets = budgetMock.domainListMock();
        given(budgetUseCase.getBudgetList(anyLong())).willReturn(budgets);

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/budget/list")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.length()").value(budgets.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }

    @Test
    @DisplayName("예산 수정 테스트 : 성공")
    @MockCustomUser
    void update_budget_success_test() throws Exception {
        // given
        Budget updatedBudget = budgetMock.domainMock();
        UpdateBudgetRequest requestDto = budgetMock.UpdateRequestDto();
        String content = objectMapper.writeValueAsString(requestDto);

        given(budgetUseCase.updateBudget(anyLong(), anyLong(), any(UpdateBudgetRequest.class)))
                .willReturn(updatedBudget);

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/budget" + "/{budgetId}", 1L)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/budget/" + updatedBudget.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.budgetId").value(budgetMock.domainMock().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }

    @Test
    @DisplayName("예산 삭제 테스트 : 성공")
    @MockCustomUser
    void delete_budget_success_test() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/budget" + "/{budgetId}", 1L));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }

    @Test
    @DisplayName("예산 추천 테스트 : 성공")
    @MockCustomUser
    void get_recommend_budget_success_test() throws Exception {
        // given
        Long amount = 600000L;
        given(budgetUseCase.getRecommendBudgetByCategory(amount)).willReturn(budgetMock.recommendedBudgetListMock());

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/budget/recommendation" + "?amount=" + amount));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.length()").value(budgetMock.recommendedBudgetListMock().size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }
}