package Entities;

import java.time.LocalDate;

public class HealthMetrics {
    private final String userId;
    private final LocalDate date;
    private double sleepHour;
    private int steps;
    private double waterLitres;
    private double exerciseMinutes;
    private int calories;

    public HealthMetrics(String userId, LocalDate date, double sleepHour, int steps,
                         double waterLitres, double exerciseMinutes, int calories) {
        this.userId = userId;
        this.date = date;
        this.sleepHour = sleepHour;
        this.steps = steps;
        this.waterLitres = waterLitres;
        this.exerciseMinutes = exerciseMinutes;
        this.calories = calories;
    }

    public String getUserId() { return userId; }
    public LocalDate getDate() { return date; }
    public double getSleepHour() { return sleepHour; }
    public int getSteps() { return steps; }
    public double getWaterLitres() { return waterLitres; }
    public double getExerciseMinutes() { return exerciseMinutes; }
    public int getCalories() { return calories; }


    public double getSleepHours() { return sleepHour; }        // For DailyHealthScore
    public double getWaterIntake() { return waterLitres; }     // For DailyHealthScore

    public void setSleepHour(double sleepHour) {
        if (sleepHour < 0 || sleepHour > 24) {
            throw new IllegalArgumentException("Sleep hours must be between 0 and 24");
        }
        this.sleepHour = sleepHour;
    }

    public void setSteps(int steps) {
        if (steps < 0) {
            throw new IllegalArgumentException("Steps cannot be negative");
        }
        this.steps = steps;
    }

    public void setWaterLitres(double waterLitres) {
        if (waterLitres < 0) {
            throw new IllegalArgumentException("Water intake cannot be negative");
        }
        this.waterLitres = waterLitres;
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

    public boolean validateMetrics() {
        try {
            setSleepHour(this.sleepHour);
            setSteps(this.steps);
            setWaterLitres(this.waterLitres);
            setExerciseMinutes(this.exerciseMinutes);
            setCalories(this.calories);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getSummary() {
        return String.format(
                "Health Summary for %s: Sleep: %.1f hours, Steps: %d, Water: %.1fL, Exercise: %.1f minutes, Calories: %d",
                date, sleepHour, steps, waterLitres, exerciseMinutes, calories
        );
    }

    @Override
    public String toString() {
        return getSummary();
    }
}