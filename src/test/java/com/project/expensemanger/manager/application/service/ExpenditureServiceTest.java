package com.project.expensemanger.manager.application.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.CategoryErrorCode;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.application.mock.CategoryMock;
import com.project.expensemanger.manager.application.mock.ExpenditureMock;
import com.project.expensemanger.manager.application.port.out.CategoryPort;
import com.project.expensemanger.manager.application.port.out.ExpenditurePort;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenditureServiceTest {

    @InjectMocks
    private ExpenditureService sut;

    @Mock
    private ExpenditurePort expenditurePort;

    @Mock
    private CategoryPort categoryPort;

    private ExpenditureMock expenditureMock = new ExpenditureMock();

    private CategoryMock categoryMock = new CategoryMock();

    @Test
    @DisplayName("지출 등록 테스트 : 성공")
    void expenditure_save_success_test() throws Exception {
        // given
        Long userId = expenditureMock.getId();
        RegisterExpenditure registerExpenditure = expenditureMock.RegisterRequestDto();
        Expenditure expenditure = expenditureMock.toDomain();

        given(categoryPort.findById(anyLong())).willReturn(categoryMock.standardDomainMock());
        given(expenditurePort.save(any(Expenditure.class))).willReturn(expenditure);

        // when
        Long savedExpenditureId = sut.registerExpenditure(userId, registerExpenditure);

        // then
        assertThat(savedExpenditureId).isEqualTo(expenditure.getId());
    }

    @Test
    @DisplayName("지출 등록 테스트 : 실패 [카테고리가 존재하지 않을 경우]")
    void expenditure_save_failure_when_category_not_exist() throws Exception {
        // given
        Long userId = expenditureMock.getId();
        RegisterExpenditure registerExpenditure = expenditureMock.RegisterRequestDto();

        doThrow(new BaseException(CategoryErrorCode.CATEGORY_NOT_FOUND)).when(categoryPort).findById(anyLong());

        // when & then
        assertThatThrownBy(() -> sut.registerExpenditure(userId, registerExpenditure))
                .isInstanceOf(BaseException.class)
                .hasMessage(CategoryErrorCode.CATEGORY_NOT_FOUND.getMessage());
    }
}