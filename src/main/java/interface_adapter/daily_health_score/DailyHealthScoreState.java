package interface_adapter.daily_health_score;

import Entities.HealthMetrics;
import Entities.HealthScore;

import java.time.LocalDate;

public class DailyHealthScoreState {

    private HealthMetrics healthMetrics;   // newly added
    private int score;
    private String summaryFeedback;      // short interpretation of score
    private LocalDate date;
    private String errorMessage;         // non-null only if failure occurred

    public DailyHealthScoreState() {
        this.healthMetrics = null;    // new
        this.score = 0;
        this.summaryFeedback = null;
        this.date = null;
        this.errorMessage = null;
    }

    // getters

    public HealthMetrics getHealthMetrics() {return healthMetrics;}  // new

    public int getScore() {
        return score;
    }

    public String getSummaryFeedback() {
        return summaryFeedback;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // setters

    public void setHealthMetrics(HealthMetrics healthMetrics) {
        this.healthMetrics = healthMetrics;      // new
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSummaryFeedback(String summaryFeedback) {
        this.summaryFeedback = summaryFeedback;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
