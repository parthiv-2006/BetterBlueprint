package use_case.input_metrics;

/**
 * Input data for the Input Metrics use case.
 */
public class InputMetricsInputData {
    private final String username;
    private final double sleepHours;
    private final double waterIntake;
    private final int caloriesConsumed;
    private final double exerciseDuration;

    public InputMetricsInputData(String username, double sleepHours, double waterIntake,
                                 int caloriesConsumed, double exerciseDuration) {
        this.username = username;
        this.sleepHours = sleepHours;
        this.waterIntake = waterIntake;
        this.caloriesConsumed = caloriesConsumed;
        this.exerciseDuration = exerciseDuration;
    }

    public String getUsername() {
        return username;
    }

    public double getSleepHours() {
        return sleepHours;
    }

    public double getWaterIntake() {
        return waterIntake;
    }

    public int getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public double getExerciseDuration() {
        return exerciseDuration;
    }
}

