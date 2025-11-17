package main.java.Entities;


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
    private int calories;

    // Constructor
    public HealthMetrics(String userId, LocalDate date, double sleepHours, double waterIntake,
                         double exerciseMinutes, int calories) {
        this.userId = userId;
        this.date = date;
        setSleepHours(sleepHours);
        setWaterIntake(waterIntake);
        setExerciseMinutes(exerciseMinutes);
        setCalories(calories);
    }

    // Getters
    public String getUserId() { return userId; }
    public LocalDate getDate() { return date; }
    public double getSleepHours() { return sleepHours; }
    public double getWaterIntake() { return waterIntake; }
    public double getExerciseMinutes() { return exerciseMinutes; }
    public int getCalories() { return calories; }

    // Setters
    public void setSleepHours(double sleepHours) {
        if (sleepHours < 0 || sleepHours > 24) {
            throw new IllegalArgumentException("Sleep hours must be between 0 and 24");
        }
        this.sleepHours = sleepHours;
    }

    public void setWaterIntake(double waterIntake) {
        if (waterIntake < 0) {
            throw new IllegalArgumentException("Water intake cannot be negative");
        }
        this.waterIntake = waterIntake;
    }

    public void setExerciseMinutes(double exerciseMinutes) {
        if (exerciseMinutes < 0) {
            throw new IllegalArgumentException("Exercise minutes cannot be negative");
        }
        this.exerciseMinutes = exerciseMinutes;
    }

    public void setCalories(int calories) {
        if (calories < 0) {
            throw new IllegalArgumentException("Calories cannot be negative");
        }
        this.calories = calories;
    }

    /**
     * Validates all health metrics
     * @return true if all metrics are valid, false otherwise
     */
    public boolean validateMetrics() {
        try {
            setSleepHours(this.sleepHours);
            setWaterIntake(this.waterIntake);
            setExerciseMinutes(this.exerciseMinutes);
            setCalories(this.calories);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Returns a summary string of the health metrics
     */
    public String getSummary() {
        return String.format(
                "Health Summary for %s: Sleep: %.1f hours, Water: %.1fL, Exercise: %.1f minutes, Calories: %d",
                date, sleepHours, waterIntake, exerciseMinutes, calories
        );
    }

    @Override
    public String toString() {
        return getSummary();
    }
}