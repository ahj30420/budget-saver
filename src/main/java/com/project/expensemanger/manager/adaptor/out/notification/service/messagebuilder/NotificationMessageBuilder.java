package com.project.expensemanger.manager.adaptor.out.notification.service.messagebuilder;

import com.project.expensemanger.manager.application.service.model.TodayBudgetRecommendationModel;
import com.project.expensemanger.manager.application.service.model.TodayExpenditureSummaryModel;
import org.springframework.stereotype.Service;

@Service
public class NotificationMessageBuilder {

    public String buildTodayExpenditureMessage(TodayExpenditureSummaryModel summary) {
        StringBuilder body = new StringBuilder();

        body.append("오늘 총 지출 금액: ")
            .append(String.format("%,d원", summary.totalTodayAmount()))
            .append("\n\n")
            .append("카테고리별 지출 상세:\n");

        summary.categories().forEach(cat -> {
            body.append("\n- ").append(cat.categoryName()).append("\n")
                .append("  · 오늘 지출 금액: ")
                .append(String.format("%,d원", cat.todayAmount())).append("\n")
                .append("  · 오늘 기준 적정 금액: ")
                .append(String.format("%,d원", cat.todayRecommendedAmount())).append("\n")
                .append("  · 위험도: ")
                .append(cat.riskPercentage()).append("%\n");
        });

        return body.toString();
    }

    public String buildDailyRecommendationMessage(TodayBudgetRecommendationModel recommendation) {
        StringBuilder body = new StringBuilder();
        body.append("오늘 사용 가능한 총 금액: ")
            .append(String.format("%,d원", recommendation.todayTotalPossible()))
            .append("\n\n")
            .append("카테고리별 추천 금액:\n");

        recommendation.categories().forEach(cat -> {
            body.append("\n- ").append(cat.categoryName())
                .append(": ").append(String.format("%,d원", cat.recommendedAmount()));
        });

        body.append("\n\n멘트: ").append(recommendation.message());

        return body.toString();
    }
}
