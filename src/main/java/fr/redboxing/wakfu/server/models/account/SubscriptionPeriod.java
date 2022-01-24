package fr.redboxing.wakfu.server.models.account;

import java.sql.Timestamp;

public class SubscriptionPeriod {
    private final int subscriptionLevel;
    private final Timestamp startDate;
    private final Timestamp endDate;

    public SubscriptionPeriod(int subscriptionLevel, Timestamp startDate, Timestamp endDate) {
        this.subscriptionLevel = subscriptionLevel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getSubscriptionLevel() {
        return subscriptionLevel;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }
}
