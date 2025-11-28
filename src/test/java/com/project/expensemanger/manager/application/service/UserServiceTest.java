package com.project.expensemanger.manager.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.UserErrorCode;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.SignupRequest;
import com.project.expensemanger.manager.application.mock.UserMock;
import com.project.expensemanger.manager.application.port.out.UserPort;
import com.project.expensemanger.manager.domain.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService sut;

    @Mock
    UserPort userPort;

    @Mock
    PasswordEncoder passwordEncoder;

    UserMock mock;

    @BeforeEach
    void setUp() {
        mock = new UserMock(passwordEncoder);
    }


    @Test
    @DisplayName("사용자 등록 테스트 : 성공")
    void register_user_success_test() {
        // given
        User saveMock = mock.domainMock();
        SignupRequest requestDto = new SignupRequest(
               mock.getEmail(),
                mock.getBeforeEncodedPw(),
                mock.getName());

        given(userPort.save(any(User.class))).willReturn(saveMock);

        // when
        Long userId = sut.registerUser(requestDto);

        // then
        assertThat(userId).isEqualTo(saveMock.getId());
    }

    @Test
    @DisplayName("사용자 등록 테스트 : 실패")
    void register_user_fail_test() {
        // given
        SignupRequest requestDto = new SignupRequest(
                mock.getEmail(),
                mock.getBeforeEncodedPw(),
                mock.getName()
        );

        doThrow(new BaseException(UserErrorCode.EMAIL_ALREADY_EXISTS))
                .when(userPort).assertEmailNotExists(any(String.class));

        // when & then
        assertThrows(BaseException.class, () -> { sut.registerUser(requestDto); });
    }
}