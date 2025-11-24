package use_case.daily_health_score;

import Entities.HealthMetrics;

import java.time.LocalDate;

/**
 * The Input Data for the Daily Health Score Use Case.
 */

public class DailyHealthScoreInputData {
    private final LocalDate date;
    private final String userId;

    public DailyHealthScoreInputData(LocalDate date, String userId) {
        this.date = date;
        this.userId = userId;
    }

    public LocalDate getDate() { return date; }
    public String getUserId() { return userId; }
}