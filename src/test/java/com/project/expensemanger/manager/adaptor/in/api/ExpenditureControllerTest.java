package com.project.expensemanger.manager.adaptor.in.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.core.common.security.annotation.MockCustomUser;
import com.project.expensemanger.core.common.security.config.AuthTestConfig;
import com.project.expensemanger.core.config.SecurityConfig;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.adaptor.in.api.mapper.ExpenditureMapper;
import com.project.expensemanger.manager.application.mock.ExpenditureMock;
import com.project.expensemanger.manager.application.port.in.ExpenditureUseCase;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
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

@WebMvcTest(ExpenditureController.class)
@Import({SecurityConfig.class, AuthTestConfig.class, ExpenditureMapper.class, ExpenditureMock.class})
@AutoConfigureMockMvc
class ExpenditureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExpenditureMock expenditureMock;

    @MockitoBean
    private ExpenditureUseCase expenditureUseCase;


    @Test
    @DisplayName("지출 등록 테스트 : 성공")
    @MockCustomUser
    void expenditure_register_success_test() throws Exception {
        // given
        Expenditure expenditure = expenditureMock.toDomain();
        RegisterExpenditure registerExpenditure = expenditureMock.RegisterRequestDto();
        String content = objectMapper.writeValueAsString(registerExpenditure);

        given(expenditureUseCase.registerExpenditure(anyLong(), any(RegisterExpenditure.class)))
                .willReturn(expenditure.getId());

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/expenditure")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.expenditureId").value(expenditure.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }
}