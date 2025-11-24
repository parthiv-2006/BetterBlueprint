package use_case.daily_health_score;

public class DailyMetricsDTO {
    private final double sleepHours;
    private final double exerciseMinutes;
    private final int calories;
    private final double waterIntake;

    public DailyMetricsDTO(
            double sleepHours,
            double exerciseMinutes,
            int calories,
            double waterIntake
    ) {
        this.sleepHours = sleepHours;
        this.exerciseMinutes = exerciseMinutes;
        this.calories = calories;
        this.waterIntake = waterIntake;
    }

    public double getSleepHours() { return sleepHours; }
    public double getExerciseMinutes() { return exerciseMinutes; }
    public int getCalories() { return calories; }
    public double getWaterIntake() { return waterIntake; }
}

