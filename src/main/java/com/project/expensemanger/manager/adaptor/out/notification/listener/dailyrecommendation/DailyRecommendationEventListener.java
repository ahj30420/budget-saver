package com.project.expensemanger.manager.adaptor.out.notification.listener.dailyrecommendation;

import com.project.expensemanger.manager.application.service.notification.event.DailyRecommendationEvent;
import org.springframework.context.event.EventListener;

public interface DailyRecommendationEventListener {
    @EventListener
    void handle(DailyRecommendationEvent event);
}
