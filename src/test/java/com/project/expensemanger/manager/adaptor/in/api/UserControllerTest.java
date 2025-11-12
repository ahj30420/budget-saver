package com.project.expensemanger.manager.adaptor.in.api;

import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.core.common.security.config.AuthTestConfig;
import com.project.expensemanger.core.config.JacksonConfig;
import com.project.expensemanger.core.config.SecurityConfig;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.SignupRequest;
import com.project.expensemanger.manager.application.mock.UserMock;
import com.project.expensemanger.manager.application.port.in.UserUseCase;
import com.project.expensemanger.manager.domain.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@Import({AuthTestConfig.class, SecurityConfig.class, JacksonConfig.class})
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserMock userMock;

    @Autowired
    UserUseCase userUseCase;

    @Test
    @DisplayName("회원 가입 테스트 : 성공")
    void signup_success_test() throws Exception {
        // given
        SignupRequest signupDto = userMock.signupMock();
        User mockUser = userMock.domainMock();
        String content = objectMapper.writeValueAsString(signupDto);

        given(userUseCase.registerUser(signupDto)).willReturn(mockUser.getId());

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/signup")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(mockUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }

    @Test
    @DisplayName("회원 가입 테스트 : 실패 [비밀 번호 길이 에러]")
    void signup_fail_password_length_test() throws Exception {
        // given
        SignupRequest signupRequest = userMock.signupLineWrongPasswordMock();
        String content = objectMapper.writeValueAsString(signupRequest);

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/signup")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ERROR"));
    }

    @Test
    @DisplayName("회원 가입 테스트 : 실패 [비밀 번호 타입(최소 2개 이상) 에러]")
    void signup_fail_password_type_test() throws Exception {
        // given
        SignupRequest signupDto = userMock.signupNotTwoTypePasswordMock();
        String content = objectMapper.writeValueAsString(signupDto);

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/signup")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ERROR"));
    }

}