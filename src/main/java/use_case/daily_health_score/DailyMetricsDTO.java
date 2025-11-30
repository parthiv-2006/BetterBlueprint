package use_case.daily_health_score;

/**
 * The Data Transfer Object for Daily Health Metrics.
 */

public class DailyMetricsDTO {
    private final double sleepHours;
    private final double exerciseMinutes;
    private final int calories;
    private final double waterIntake;
    private final int steps;

    public DailyMetricsDTO(
            double sleepHours,
            double exerciseMinutes,
            int calories,
            double waterIntake,
            int steps
    ) {
        this.sleepHours = sleepHours;
        this.exerciseMinutes = exerciseMinutes;
        this.calories = calories;
        this.waterIntake = waterIntake;
        this.steps = steps;
    }

    public double getSleepHours() { return sleepHours; }
    public double getExerciseMinutes() { return exerciseMinutes; }
    public int getCalories() { return calories; }
    public double getWaterIntake() { return waterIntake; }
    public int getSteps() { return steps; }
}
