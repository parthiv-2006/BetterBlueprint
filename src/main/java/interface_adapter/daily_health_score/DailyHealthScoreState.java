package interface_adapter.daily_health_score;

import java.time.LocalDate;

public class DailyHealthScoreState {

    private String userId;
    private LocalDate date;
    private Integer score;
    private String feedback;
    private String errorMessage;

    // Health metrics used for score calculation
    private Double sleepHours;
    private Double exerciseMinutes;
    private Integer calories;
    private Double waterIntake;

    public DailyHealthScoreState() {
        this.userId = "";
        this.date = null;
        this.score = null;
        this.feedback = "";
        this.errorMessage = null;
        this.sleepHours = null;
        this.exerciseMinutes = null;
        this.calories = null;
        this.waterIntake = null;
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

    public Double getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(Double sleepHours) {
        this.sleepHours = sleepHours;
    }

    public Double getExerciseMinutes() {
        return exerciseMinutes;
    }

    public void setExerciseMinutes(Double exerciseMinutes) {
        this.exerciseMinutes = exerciseMinutes;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Double getWaterIntake() {
        return waterIntake;
    }

    public void setWaterIntake(Double waterIntake) {
        this.waterIntake = waterIntake;
    }
}