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

    // Optional: target weight (as a string for UI, e.g. "70")
    private final String target;

    // Optional: timeframe in weeks (as a string for UI, e.g. "8")
    private final String timeframe;

    /**
     * Original constructor (no target, no timeframe).
     * This stays so all existing code that passes 7 arguments still compiles.
     */
    public GoalsOutputData(String goalType,
                           String dailyIntakeCalories,
                           String dailyBurnCalories,
                           String explanation,
                           int currentWeightKg,
                           boolean shouldRedirectToSettings,
                           String redirectMessage) {
        this(goalType,
                dailyIntakeCalories,
                dailyBurnCalories,
                explanation,
                currentWeightKg,
                shouldRedirectToSettings,
                redirectMessage,
                "",    // default target
                "");   // default timeframe
    }

    /**
     * New constructor including target and timeframe.
     * Use this if you want to carry both through the use case.
     */
    public GoalsOutputData(String goalType,
                           String dailyIntakeCalories,
                           String dailyBurnCalories,
                           String explanation,
                           int currentWeightKg,
                           boolean shouldRedirectToSettings,
                           String redirectMessage,
                           String target,
                           String timeframe) {
        this.goalType = goalType;
        this.dailyIntakeCalories = dailyIntakeCalories;
        this.dailyBurnCalories = dailyBurnCalories;
        this.explanation = explanation;
        this.currentWeightKg = currentWeightKg;
        this.shouldRedirectToSettings = shouldRedirectToSettings;
        this.redirectMessage = redirectMessage;
        this.target = target;
        this.timeframe = timeframe;
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

    public String getTarget() {
        return target;
    }

    public String getTimeframe() {
        return timeframe;
    }
}
