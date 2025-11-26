package use_case.input_metrics;

public class InputMetricsInputData {
    private final String username;
    private final float sleepHours;
    private final int steps; // ADDED steps field
    private final float waterIntake;
    private final int calories;
    private final float exerciseDuration;

    public InputMetricsInputData(String username, float sleepHours, int steps,
                                 float waterIntake, int calories, float exerciseDuration) {
        this.username = username;
        this.sleepHours = sleepHours;
        this.steps = steps; // ADDED steps parameter
        this.waterIntake = waterIntake;
        this.calories = calories;
        this.exerciseDuration = exerciseDuration;
    }

    public String getUsername() {
        return username;
    }

    public float getSleepHours() {
        return sleepHours;
    }

    public int getSteps() { // ADDED getter for steps
        return steps;
    }

    public float getWaterIntake() {
        return waterIntake;
    }

    public int getCalories() {
        return calories;
    }

    public float getExerciseDuration() {
        return exerciseDuration;
    }
}