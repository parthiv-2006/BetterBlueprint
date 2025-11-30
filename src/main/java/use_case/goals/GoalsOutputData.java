package use_case.goals;

public class GoalsOutputData {

    private final String goalType;
    private final String dailyIntakeCalories;
    private final String dailyBurnCalories;
    private final String explanation;

    private final int currentWeightKg;

    private final boolean shouldRedirectToSettings;
    private final String redirectMessage;

    private final String target;

    private final String timeframe;

    public GoalsOutputData(String goalType,
                           String dailyIntakeCalories,
                           String dailyBurnCalories,
                           String explanation,
                           int currentWeightKg,
                           boolean shouldRedirectToSettings,
                           String redirectMessage) {
        this(
                goalType,
                dailyIntakeCalories,
                dailyBurnCalories,
                explanation,
                currentWeightKg,
                shouldRedirectToSettings,
                redirectMessage,
                "",
                ""
        );
    }

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
