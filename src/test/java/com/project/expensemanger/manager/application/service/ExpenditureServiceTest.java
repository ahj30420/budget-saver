package com.project.expensemanger.manager.application.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.CategoryErrorCode;
import com.project.expensemanger.core.common.exception.errorcode.ExpenditureErrorCode;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateExpenditureRequest;
import com.project.expensemanger.manager.application.mock.CategoryMock;
import com.project.expensemanger.manager.application.mock.ExpenditureMock;
import com.project.expensemanger.manager.application.model.ExpenditureDetailModel;
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
        Long userId = expenditureMock.getUserId();
        RegisterExpenditure registerExpenditure = expenditureMock.RegisterRequestDto();
        Expenditure expenditure = expenditureMock.domainMock();

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
        Long userId = expenditureMock.getUserId();
        RegisterExpenditure registerExpenditure = expenditureMock.RegisterRequestDto();

        doThrow(new BaseException(CategoryErrorCode.CATEGORY_NOT_FOUND)).when(categoryPort).findById(anyLong());

        // when & then
        assertThatThrownBy(() -> sut.registerExpenditure(userId, registerExpenditure))
                .isInstanceOf(BaseException.class)
                .hasMessage(CategoryErrorCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("지출 삭제 테스트 : 성공")
    void expenditure_delete_succes_test() throws Exception {
        // given
        Expenditure expenditure = expenditureMock.domainMock();

        given(expenditurePort.findById(any(Long.class))).willReturn(expenditure);
        willDoNothing().given(expenditurePort).delete(any(Expenditure.class));

        // when
        sut.deleteExpenditure(expenditure.getUserId(), expenditure.getId());

        // then
        verify(expenditurePort, times(1)).delete(any(Expenditure.class));
    }

    @Test
    @DisplayName("지출 삭제 테스트 : 실패[해당 지출을 찾지 못했을 경우]")
    void expenditure_delete_fail_when_expenditure_not_exist() throws Exception {
        // given
        Long userId = expenditureMock.getUserId();
        Long expenditureId = expenditureMock.getId();

        doThrow(new BaseException(ExpenditureErrorCode.EXPENDITURE_NOT_FOUND))
                .when(expenditurePort)
                .findById(anyLong());

        // when & then
        assertThatThrownBy(() -> sut.deleteExpenditure(userId, expenditureId))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.EXPENDITURE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("지출 삭제 테스트 : 실패 [해당 사용자가 아닐 경우]")
    void expenditure_delete_fail_when_user_is_not_owner() throws Exception {
        // given
        Long otherUserId = expenditureMock.getOtherUserId();
        Long expenditureId = expenditureMock.getId();
        Expenditure expenditure = expenditureMock.domainMock();

        given(expenditurePort.findById(any(Long.class))).willReturn(expenditure);

        // when & then
        assertThatThrownBy(() -> sut.deleteExpenditure(otherUserId, expenditureId))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.EXPENDITURE_FORBIDDEN.getMessage());
    }

    @Test
    @DisplayName("지출 수정 테스트 : 성공")
    void expenditure_update_success_test() throws Exception {
        // given
        Long userId = expenditureMock.getUserId();
        Long expenditureId = expenditureMock.getId();
        UpdateExpenditureRequest requestDto = expenditureMock.updateRequestDto();

        given(expenditurePort.findById(anyLong())).willReturn(expenditureMock.domainMock());
        willDoNothing().given(expenditurePort).update(any(Expenditure.class));

        // when
        Expenditure changedExpenditure = sut.updateExpenditure(userId, expenditureId, requestDto);

        // then
        assertThat(changedExpenditure.getAmount()).isEqualTo(requestDto.amount());
        assertThat(changedExpenditure.getSpentAt()).isEqualTo(requestDto.spentAt());
        assertThat(changedExpenditure.getMemo()).isEqualTo(requestDto.memo());
        assertThat(changedExpenditure.getCategoryId()).isEqualTo(requestDto.categoryId());
        assertThat(changedExpenditure.isExcludedFromTotal()).isEqualTo(requestDto.excludedFromTotal());
    }

    @Test
    @DisplayName("지출 수정 테스트 : 실패[해당 지출이 조회되지 않을 경우]")
    void expenditure_update_fail_when_expenditure_not_exist() throws Exception {
        // given
        Long userId = expenditureMock.getUserId();
        Long expenditureId = expenditureMock.getId();
        UpdateExpenditureRequest requestDto = expenditureMock.updateRequestDto();

        doThrow(new BaseException(ExpenditureErrorCode.EXPENDITURE_NOT_FOUND))
                .when(expenditurePort)
                .findById(anyLong());

        // when & then
        assertThatThrownBy(() -> sut.updateExpenditure(userId, expenditureId, requestDto))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.EXPENDITURE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("지출 수정 테스트 : 실패 [카테고리가 존재하지 않을 경우]")
    void expenditure_update_fail_when_category_not_exist() throws Exception {
        // given
        Long userId = expenditureMock.getUserId();
        Long expenditureId = expenditureMock.getId();
        UpdateExpenditureRequest requestDto = expenditureMock.updateRequestDto();

        doThrow(new BaseException(CategoryErrorCode.CATEGORY_NOT_FOUND)).when(categoryPort).findById(anyLong());

        // when & then
        assertThatThrownBy(() -> sut.updateExpenditure(userId, expenditureId, requestDto))
                .isInstanceOf(BaseException.class)
                .hasMessage(CategoryErrorCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("지출 수정 테스트 : 실패 [해당 사용자가 아닐 경우]")
    void expenditure_update_fail_when_user_is_not_owner() throws Exception {
        // given
        Long otherUserId = expenditureMock.getOtherUserId();
        Long expenditureId = expenditureMock.getId();
        UpdateExpenditureRequest requestDto = expenditureMock.updateRequestDto();

        given(expenditurePort.findById(anyLong())).willReturn(expenditureMock.domainMock());

        // when & then
        assertThatThrownBy(() -> sut.updateExpenditure(otherUserId, expenditureId, requestDto))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.EXPENDITURE_FORBIDDEN.getMessage());
    }

    @Test
    @DisplayName("지출 상세 정보 조회 테스트 : 성공")
    void expenditure_get_details_succes() throws Exception {
        // given
        Long userId = expenditureMock.getUserId();
        Long expenditureId = expenditureMock.getId();
        ExpenditureDetailModel expenditureDetailModel = expenditureMock.ExpenditureDetailModel();

        given(expenditurePort.getDetails(anyLong())).willReturn(expenditureDetailModel);

        // when
        ExpenditureDetailModel result = sut.getExpenditureDetails(userId, expenditureId);

        // then
        assertThat(result).isEqualTo(expenditureDetailModel);
    }

    @Test
    @DisplayName("지출 상세 정보 조회 테스트 : 실패 [해당 사용자가 아닐 경우]")
    void expenditure_get_details_fail_when_user_is_not_owner() throws Exception {
        // given
        Long otherUserId = expenditureMock.getOtherUserId();
        Long expenditureId = expenditureMock.getId();
        ExpenditureDetailModel expenditureDetailModel = expenditureMock.ExpenditureDetailModel();

        given(expenditurePort.getDetails(anyLong())).willReturn(expenditureDetailModel);

        // when & then
        assertThatThrownBy(() -> sut.getExpenditureDetails(otherUserId, expenditureId))
                .isInstanceOf(BaseException.class)
                .hasMessage(ExpenditureErrorCode.EXPENDITURE_FORBIDDEN.getMessage());
    }
}