package use_case.daily_health_score;

import java.time.LocalDate;
import Entities.HealthMetrics;

/**
 * Output Data for the Daily Health Score Use Case.
Holds the calculated score, feedback, and wraps all raw metrics in a HealthMetrics entity.
 */

public class DailyHealthScoreOutputData {
    private final LocalDate date;
    private final String userId;
    private final int score;
    private final String feedback;
    private final HealthMetrics metrics;

    public DailyHealthScoreOutputData(
            LocalDate date,
            String userId,
            int score,
            String feedback,
            HealthMetrics metrics
    ) {
        this.date = date;
        this.userId = userId;
        this.score = score;
        this.feedback = feedback;
        this.metrics = metrics;
    }

    public LocalDate getDate() { return date; }
    public String getUserId() { return userId; }
    public int getScore() { return score; }
    public String getFeedback() { return feedback; }

    public HealthMetrics getMetrics() { return metrics; }
}