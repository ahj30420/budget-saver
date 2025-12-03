package com.project.expensemanger.manager.adaptor.out.notification.listener.todayexpenditure.implement;

import com.project.expensemanger.manager.adaptor.out.jpa.user.UserJpaRepository;
import com.project.expensemanger.manager.adaptor.out.notification.listener.todayexpenditure.TodayExpenditureEventListener;
import com.project.expensemanger.manager.adaptor.out.notification.service.EmailService;
import com.project.expensemanger.manager.adaptor.out.notification.service.messagebuilder.NotificationMessageBuilder;
import com.project.expensemanger.manager.application.service.notification.event.TodayExpenditureEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailTodayExpenditureListener implements TodayExpenditureEventListener {

    private final EmailService emailService;
    private final UserJpaRepository userJpaRepository;
    private final NotificationMessageBuilder messageBuilder;

    @Async
    @Override
    public void handle(TodayExpenditureEvent event) {
        String email = userJpaRepository.findById(event.userId())
                .map(u -> u.getEmail())
                .orElse(null);

        if (!isValidEmail(email)) return;

        var summary = event.todayExpenditure();
        if (summary.totalTodayAmount() == 0) return;

        emailService.send(email, "오늘의 지출 안내",
                messageBuilder.buildTodayExpenditureMessage(summary));
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isBlank();
    }
}
