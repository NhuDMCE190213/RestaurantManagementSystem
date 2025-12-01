package model;

import java.time.LocalDate;

/**
 * Stores headline metrics for a daily kitchen usage report.
 */
public class DailyUsageSummary {
    private LocalDate reportDate;
    private int completedOrders;
    private int itemsPrepared;

    public DailyUsageSummary() {
    }

    public DailyUsageSummary(LocalDate reportDate, int completedOrders, int itemsPrepared) {
        this.reportDate = reportDate;
        this.completedOrders = completedOrders;
        this.itemsPrepared = itemsPrepared;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }

    public int getItemsPrepared() {
        return itemsPrepared;
    }

    public void setItemsPrepared(int itemsPrepared) {
        this.itemsPrepared = itemsPrepared;
    }
}
