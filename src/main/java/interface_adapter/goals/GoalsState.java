package interface_adapter.goals;

public class GoalsState {
    private String goalType;
    private String targetWeight;
    private String timeframe;
    private String errorMessage;
    private String dailyIntakeCalories;
    private String dailyBurnCalories;
    private String explanation;
    private boolean shouldRedirectToSettings = false;
    private String redirectMessage = null;
    private String currentWeightLabel;


    public GoalsState() {}

    public String getGoalType() { return goalType; }
    public void setGoalType(String goalType) { this.goalType = goalType; }

    public String getTargetWeight() { return targetWeight; }
    public void setTargetWeight(String targetWeight) { this.targetWeight = targetWeight; }

    public String getTimeframe() { return timeframe; }
    public void setTimeframe(String timeframe) { this.timeframe = timeframe; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getDailyIntakeCalories() { return dailyIntakeCalories; }
    public void setDailyIntakeCalories(String value) { this.dailyIntakeCalories = value; }

    public String getDailyBurnCalories() { return dailyBurnCalories; }
    public void setDailyBurnCalories(String value) { this.dailyBurnCalories = value; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String value) { this.explanation = value; }

    public boolean shouldRedirectToSettings() {
        return shouldRedirectToSettings;
    }

    public void setShouldRedirectToSettings(boolean value) {
        this.shouldRedirectToSettings = value;
    }

    public String getRedirectMessage() {
        return redirectMessage;
    }

    public void setRedirectMessage(String redirectMessage) {
        this.redirectMessage = redirectMessage;
    }

    public String getCurrentWeightLabel() {
        return currentWeightLabel;
    }

    public void setCurrentWeightLabel(String currentWeightLabel) {
        this.currentWeightLabel = currentWeightLabel;
    }
}
