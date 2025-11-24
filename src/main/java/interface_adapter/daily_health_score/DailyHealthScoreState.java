package interface_adapter.daily_health_score;

import java.time.LocalDate;

public class DailyHealthScoreState {

    private String userId;
    private LocalDate date;
    private Integer score;
    private String feedback;
    private String errorMessage;

    public DailyHealthScoreState() {
        this.userId = "";
        this.date = null;
        this.score = null;
        this.feedback = "";
        this.errorMessage = null;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
