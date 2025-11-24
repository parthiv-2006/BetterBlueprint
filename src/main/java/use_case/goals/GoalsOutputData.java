package use_case.goals;

public class GoalsOutputData {
    private final String dailyIntakeCalories;
    private final String dailyBurnCalories;
    private final String explanation;

    public GoalsOutputData(String dailyIntakeCalories, String dailyBurnCalories, String explanation) {
        this.dailyIntakeCalories = dailyIntakeCalories;
        this.dailyBurnCalories = dailyBurnCalories;
        this.explanation = explanation;
    }

    public String getDailyIntakeCalories() { return dailyIntakeCalories; }
    public String getDailyBurnCalories() { return dailyBurnCalories; }
    public String getExplanation() { return explanation; }
}
