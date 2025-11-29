package use_case.daily_health_score;

import java.time.LocalDate;

/**
 * Output Data for the Daily Health Score Use Case.
 */

public class DailyHealthScoreOutputData {
    private LocalDate date;
    private final String userId;
    private final int score;              // from Gemini
    private final String feedback;        // short summary
    private final double sleepHours;
    private final double waterIntake; // in liters
    private final double exerciseMinutes; // in minutes
    private final int calories;;

    public DailyHealthScoreOutputData(
            LocalDate date,
            String userId,
            int score,
            String feedback,
            double sleepHours,
            double exerciseMinutes,
            int calories,
            double waterIntake
    ) {
        this.date = date;
        this.userId = userId;
        this.score = score;
        this.feedback = feedback;
        this.sleepHours = sleepHours;
        this.exerciseMinutes = exerciseMinutes;
        this.calories = calories;
        this.waterIntake = waterIntake;
    }

    public LocalDate getDate() { return date; }
    public String getUserId() { return userId; }
    public int getScore() { return score; }
    public String getFeedback() { return feedback; }
    public double getSleepHours() { return sleepHours; }
    public double getExerciseMinutes() { return exerciseMinutes; }
    public int getCalories() { return calories; }
    public double getWaterIntake() { return waterIntake; }
}