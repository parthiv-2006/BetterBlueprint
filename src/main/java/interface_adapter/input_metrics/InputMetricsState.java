package interface_adapter.input_metrics;

public class InputMetricsState {
    private String username = "";
    private String sleepHours = "";
    private String steps = "";
    private String waterIntake = "";
    private String caloriesConsumed = "";
    private String exerciseDuration = "";
    private String errorMessage = "";

    public InputMetricsState() {
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getSleepHours() { return sleepHours; }
    public void setSleepHours(String sleepHours) { this.sleepHours = sleepHours; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public String getWaterIntake() { return waterIntake; }
    public void setWaterIntake(String waterIntake) { this.waterIntake = waterIntake; }

    public String getCaloriesConsumed() { return caloriesConsumed; }
    public void setCaloriesConsumed(String caloriesConsumed) { this.caloriesConsumed = caloriesConsumed; }

    public String getExerciseDuration() { return exerciseDuration; }
    public void setExerciseDuration(String exerciseDuration) { this.exerciseDuration = exerciseDuration; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}