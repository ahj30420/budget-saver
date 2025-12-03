package com.project.expensemanger.manager.adaptor.out.notification.listener.dailyrecommendation.implement;

import com.project.expensemanger.manager.adaptor.out.jpa.user.UserJpaRepository;
import com.project.expensemanger.manager.adaptor.out.notification.listener.dailyrecommendation.DailyRecommendationEventListener;
import com.project.expensemanger.manager.adaptor.out.notification.service.EmailService;
import com.project.expensemanger.manager.adaptor.out.notification.service.messagebuilder.NotificationMessageBuilder;
import com.project.expensemanger.manager.application.service.notification.event.DailyRecommendationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailRecommendationListener implements DailyRecommendationEventListener {

    private final EmailService emailService;
    private final UserJpaRepository userJpaRepository;
    private final NotificationMessageBuilder messageBuilder;

    @Async
    @Override
    public void handle(DailyRecommendationEvent event) {
        String email = userJpaRepository.findById(event.userId())
                .map(u -> u.getEmail())
                .orElse(null);

        if (!isValidEmail(email)) return;

        var recommendation = event.recommendation();
        if (recommendation.todayTotalPossible() == 0) return;

        emailService.send(email, "오늘의 지출 추천 안내",
                messageBuilder.buildDailyRecommendationMessage(recommendation));
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isBlank();
    }
}
