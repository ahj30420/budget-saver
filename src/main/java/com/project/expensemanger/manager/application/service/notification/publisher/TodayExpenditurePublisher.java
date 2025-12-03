package com.project.expensemanger.manager.application.service.notification.publisher;

import com.project.expensemanger.manager.application.port.out.UserPort;
import com.project.expensemanger.manager.application.service.ExpenditureConsultingService;
import com.project.expensemanger.manager.application.service.notification.event.TodayExpenditureEvent;
import com.project.expensemanger.manager.domain.User.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodayExpenditurePublisher {

    private final ExpenditureConsultingService consultingService;
    private final UserPort userPort;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 20 * * *")
    public void sendTodayExpenditure() {
        List<User> users = userPort.findSubscribedUser();

        users.forEach(user -> {
            var todayExpenditure = consultingService.getTodayExpenditure(user.getId());
            eventPublisher.publishEvent(new TodayExpenditureEvent(todayExpenditure, user.getId()));
        });
    }
}
