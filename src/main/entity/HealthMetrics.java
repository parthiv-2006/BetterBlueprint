package main.entity;


import java.time.LocalDate;

/**
 * Represents a user's daily health metrics
 */
public class HealthMetrics {
    private final String userId;
    private final LocalDate date;
    private double sleepHours;
    private double waterIntake; // in liters
    private double exerciseMinutes; // in minutes

    // Constructor
    public HealthMetrics(String userId, LocalDate date) {
        this.userId = userId;
        this.date = date;
    }

    // Getters
    public String getUserId() { return userId; }
    public LocalDate getDate() { return date; }
    public double getSleepHours() { return sleepHours; }
    public double getWaterIntake() { return waterIntake; }
    public double getExerciseMinutes() { return exerciseMinutes; }

    // Setters
    public void setSleepHours(double sleepHours) {
        this.sleepHours = sleepHours;
    }

    public void setWaterIntake(double waterIntake) {
        this.waterIntake = waterIntake;
    }

    public void setExerciseMinutes(double exerciseMinutes) {
        this.exerciseMinutes = exerciseMinutes;
    }

    /**
     * Validates all health metrics
     * @return true if all metrics are valid, false otherwise
     */
    public boolean validateMetrics() {
        if (sleepHours < 0 || sleepHours > 24) {
            return false;
        }
        if (waterIntake < 0) {
            return false;
        }
        if (exerciseMinutes < 0) {
            return false;
        }
        return true;
    }

    /**
     * Returns a summary string of the health metrics
     * @return formatted summary string
     */
    public String getSummary() {
        return String.format(
                "Health Summary for %s: Sleep: %.1f hours, Water: %.1fL, Exercise: %.1f minutes",
                date, sleepHours, waterIntake, exerciseMinutes
        );
    }
}