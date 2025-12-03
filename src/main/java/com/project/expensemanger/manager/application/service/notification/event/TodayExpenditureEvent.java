package com.project.expensemanger.manager.application.service.notification.event;

import com.project.expensemanger.manager.application.service.model.TodayExpenditureSummaryModel;

public record TodayExpenditureEvent(
        TodayExpenditureSummaryModel todayExpenditure,
        Long userId
) {
}
