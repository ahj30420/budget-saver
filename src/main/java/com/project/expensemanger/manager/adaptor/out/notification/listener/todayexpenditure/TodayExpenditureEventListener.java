package com.project.expensemanger.manager.adaptor.out.notification.listener.todayexpenditure;

import com.project.expensemanger.manager.application.service.notification.event.TodayExpenditureEvent;
import org.springframework.context.event.EventListener;

public interface TodayExpenditureEventListener {
    @EventListener
    void handle(TodayExpenditureEvent event);
}
