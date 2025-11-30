package use_case.goals;

public class GoalsOutputData {

    private final String goalType;
    private final String dailyIntakeCalories;
    private final String dailyBurnCalories;
    private final String explanation;

    // domain representation of current weight
    private final int currentWeightKg;

    // redirect to Settings if weight missing, etc.
    private final boolean shouldRedirectToSettings;
    private final String redirectMessage;

    public GoalsOutputData(String goalType,
                           String dailyIntakeCalories,
                           String dailyBurnCalories,
                           String explanation,
                           int currentWeightKg,
                           boolean shouldRedirectToSettings,
                           String redirectMessage) {
        this.goalType = goalType;
        this.dailyIntakeCalories = dailyIntakeCalories;
        this.dailyBurnCalories = dailyBurnCalories;
        this.explanation = explanation;
        this.currentWeightKg = currentWeightKg;
        this.shouldRedirectToSettings = shouldRedirectToSettings;
        this.redirectMessage = redirectMessage;
    }

    public String getGoalType() {
        return goalType;
    }

    public String getDailyIntakeCalories() {
        return dailyIntakeCalories;
    }

    public String getDailyBurnCalories() {
        return dailyBurnCalories;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getCurrentWeightKg() {
        return currentWeightKg;
    }

    public boolean shouldRedirectToSettings() {
        return shouldRedirectToSettings;
    }

    public String getRedirectMessage() {
        return redirectMessage;
    }
}
