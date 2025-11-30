package interface_adapter.goals;

public class GoalsState {
    private String goalType;
    private String target;
    private String timeframe;
    private String errorMessage;
    private String dailyIntakeCalories;
    private String dailyBurnCalories;
    private String differenceText;
    private String explanation;
    private boolean shouldRedirectToSettings = false;
    private String redirectMessage = null;
    private String currentWeightLabel;
    private int currentWeight = 0;
    private boolean resultReady = false;

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDailyIntakeCalories() {
        return dailyIntakeCalories;
    }

    public void setDailyIntakeCalories(String value) {
        this.dailyIntakeCalories = value;
    }

    public String getDailyBurnCalories() {
        return dailyBurnCalories;
    }

    public void setDailyBurnCalories(String value) {
        this.dailyBurnCalories = value;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String value) {
        this.explanation = value;
    }

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

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public String getDifferenceText() {
        return differenceText;
    }

    public void setDifferenceText(String differenceText) {
        this.differenceText = differenceText;
    }

    public boolean isResultReady() {
        return resultReady;
    }

    public void setResultReady(boolean resultReady) {
        this.resultReady = resultReady;
    }
}

