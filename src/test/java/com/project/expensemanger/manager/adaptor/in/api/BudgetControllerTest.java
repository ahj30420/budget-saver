package com.project.expensemanger.manager.adaptor.in.api;

import static org.mockito.BDDMockito.given;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.core.common.security.annotation.MockCustomUser;
import com.project.expensemanger.core.common.security.config.AuthTestConfig;
import com.project.expensemanger.core.config.JacksonConfig;
import com.project.expensemanger.core.config.SecurityConfig;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterBudgetList;
import com.project.expensemanger.manager.adaptor.in.api.mapper.BudgetMapper;
import com.project.expensemanger.manager.application.mock.BudgetMock;
import com.project.expensemanger.manager.application.port.in.BudgetUseCase;
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
@Import({SecurityConfig.class, AuthTestConfig.class, JacksonConfig.class, BudgetUseCase.class, BudgetMapper.class, BudgetMock.class})
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

}