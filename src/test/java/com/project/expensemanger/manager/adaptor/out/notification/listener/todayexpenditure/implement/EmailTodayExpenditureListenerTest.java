package com.project.expensemanger.manager.adaptor.out.notification.listener.todayexpenditure.implement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.expensemanger.manager.adaptor.out.jpa.user.UserJpaRepository;
import com.project.expensemanger.manager.adaptor.out.jpa.user.entity.UserJpaEntity;
import com.project.expensemanger.manager.adaptor.out.notification.service.EmailService;
import com.project.expensemanger.manager.adaptor.out.notification.service.messagebuilder.NotificationMessageBuilder;
import com.project.expensemanger.manager.application.port.out.UserPort;
import com.project.expensemanger.manager.application.service.ExpenditureConsultingService;
import com.project.expensemanger.manager.application.service.model.TodayExpenditureSummaryModel;
import com.project.expensemanger.manager.application.service.notification.publisher.TodayExpenditurePublisher;
import com.project.expensemanger.manager.domain.User.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@RecordApplicationEvents
@ExtendWith(SpringExtension.class)
@Import({TodayExpenditurePublisher.class, EmailTodayExpenditureListener.class})
class EmailTodayExpenditureListenerTest {

    @Autowired
    TodayExpenditurePublisher publisher;

    @Autowired
    EmailTodayExpenditureListener listener;

    @MockitoBean
    EmailService emailService;

    @MockitoBean
    UserJpaRepository userJpaRepository;

    @MockitoBean
    NotificationMessageBuilder messageBuilder;

    @MockitoBean
    ExpenditureConsultingService consultingService;

    @MockitoBean
    UserPort userPort;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    private User userStub() {
        return User.builder()
                .id(1L)
                .email("test@naver.com")
                .password("testpw1234")
                .name("테스트")
                .notificationSubscribed(true)
                .build();
    }

    private UserJpaEntity userJpaStub() {
        return UserJpaEntity.builder()
                .email("test@example.com")
                .build();
    }

    private TodayExpenditureSummaryModel todayExpenditurenStub() {
        return new TodayExpenditureSummaryModel(
                50_000,
                List.of()
        );
    }

    @Test
    @DisplayName("오늘 지출 추천 event 처리 테스트")
    void send_daily_recommendation_test() throws Exception {
        // given
        given(userPort.findSubscribedUser()).willReturn(List.of(userStub()));
        given(consultingService.getTodayExpenditure(1L)).willReturn(todayExpenditurenStub());
        given(userJpaRepository.findById(1L)).willReturn(java.util.Optional.of(userJpaStub()));
        given(messageBuilder.buildTodayExpenditureMessage(any())).willReturn("알림 메시지");

        // when
        publisher.sendTodayExpenditure();

        // then
        verify(messageBuilder, times(1)).buildTodayExpenditureMessage(any());
        verify(emailService, times(1)).send(anyString(), anyString(), anyString());
    }
}