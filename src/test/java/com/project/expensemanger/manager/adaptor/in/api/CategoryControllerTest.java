package com.project.expensemanger.manager.adaptor.in.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.core.common.exception.errorcode.AuthErrorCode;
import com.project.expensemanger.core.common.security.config.AuthTestConfig;
import com.project.expensemanger.core.config.JacksonConfig;
import com.project.expensemanger.core.config.SecurityConfig;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterCategoryRequest;
import com.project.expensemanger.manager.adaptor.in.api.mapper.CategoryMapper;
import com.project.expensemanger.manager.application.mock.CategoryMock;
import com.project.expensemanger.manager.application.port.in.CategoryUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CategoryController.class)
@Import({SecurityConfig.class, AuthTestConfig.class, JacksonConfig.class, CategoryUseCase.class, CategoryMock.class, CategoryMapper.class})
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryMock categoryMock;

    @MockitoBean
    private CategoryUseCase categoryUseCase;

    @Test
    @DisplayName("카테고리 관리자 등록 : 성공")
    @WithMockUser(roles = "USER")
    void register_standard_category_success_test() throws Exception {
        // given
        RegisterCategoryRequest request = categoryMock.standardRegisterRequestDto();
        String content = objectMapper.writeValueAsString(request);

        given(categoryUseCase.register(any(RegisterCategoryRequest.class)))
                .willReturn(categoryMock.getId());

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/category")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.categoryId").value(categoryMock.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }

        @Test
    @DisplayName("카테고리 관리자 등록 : 실패")
    @WithMockUser(roles = "USER")
    void register_standard_category_failure_test() throws Exception {
        // given
        RegisterCategoryRequest request = categoryMock.standardRegisterRequestDto();
        String content = objectMapper.writeValueAsString(request);

        given(categoryUseCase.register(any(RegisterCategoryRequest.class)))
                .willReturn(categoryMock.getId());

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/category")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.message").value(AuthErrorCode.UNAUTHENTICATED.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }

    @Test
    @DisplayName("카테고리 단건 조회 : 성공")
    @WithMockUser(roles = "USER")
    void get_category_test() throws Exception {
        // given
        given(categoryUseCase.getCategory(any(Long.class))).willReturn(categoryMock.standardDomainMock());

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/category/{categoryId}", categoryMock.getId()));

        // then
        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.categoryId").value(categoryMock.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value(categoryMock.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString());
    }

}