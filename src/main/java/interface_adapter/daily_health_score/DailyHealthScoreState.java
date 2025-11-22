package interface_adapter.daily_health_score;

import Entities.HealthScore;

public class DailyHealthScoreState {

    private HealthScore healthScore;
    private String summaryFeedback;      // short interpretation of score
    private String date;
    private String errorMessage;         // non-null only if failure occurred

    public DailyHealthScoreState() {
        this.healthScore = null;
        this.summaryFeedback = null;
        this.date = null;
        this.errorMessage = null;
    }

    // getters

    public HealthScore getHealthScore() {
        return healthScore;
    }

    public String getSummaryFeedback() {
        return summaryFeedback;
    }

    public String getDate() {
        return date;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // setters

    public void setScore(HealthScore healthScore) {
        this.healthScore = healthScore;
    }

    public void setSummaryFeedback(String summaryFeedback) {
        this.summaryFeedback = summaryFeedback;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
