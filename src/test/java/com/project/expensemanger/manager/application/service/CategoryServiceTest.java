package com.project.expensemanger.manager.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.CategoryErrorCode;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterCategoryRequest;
import com.project.expensemanger.manager.application.mock.CategoryMock;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import com.project.expensemanger.manager.domain.category.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService sut;

    @Mock
    private CategoryPort categoryPort;

    private CategoryMock categoryMock = new CategoryMock();

    @Test
    @DisplayName("카테고리 등록 테스트 : 성공")
    void register_category_success_test() throws Exception {
        // given
        RegisterCategoryRequest request = categoryMock.standardRegisterRequestDto();
        Category mockCategory = categoryMock.standardDomainMock();

        given(categoryPort.save(any(Category.class))).willReturn(mockCategory);

        // when
        Long categoryId = sut.register(request);

        // then
        assertThat(categoryId).isEqualTo(mockCategory.getId());
    }

    @Test
    void register_category_failure_test() throws Exception {
        // given
        RegisterCategoryRequest request = categoryMock.standardRegisterRequestDto();


        doThrow(new BaseException(CategoryErrorCode.CATEGORY_ALREADY_EXIST))
                .when(categoryPort)
                .assertNameNotExists(any(String.class));

        // when & then
        assertThrows(BaseException.class, () -> sut.register(request));
    }

    @Test
    @DisplayName("카테고리 단건 조회 : 성공")
    void get_category_success_test() throws Exception {
        // given
        Category mockCategory = categoryMock.standardDomainMock();
        given(categoryPort.findById(any(Long.class))).willReturn(mockCategory);

        // when
        Category result = sut.getCategory(mockCategory.getId());

        // then
        assertThat(result.getId()).isEqualTo(mockCategory.getId());
        assertThat(result.getName()).isEqualTo(mockCategory.getName());
    }

    @Test
    @DisplayName("카테고리 단건 조회 : 실패")
    void get_category_failure_test() throws Exception {
        // given
        doThrow(new BaseException(CategoryErrorCode.CATEGORY_NOT_FOUND))
                .when(categoryPort)
                .findById(any(Long.class));

        // when & then
        assertThrows(BaseException.class, () -> sut.getCategory(any(Long.class)));
    }

}