package use_case.goals;

/**
 * Input Data object for the Goals use case. Created by controllers from user input.
 */
public class GoalsInputData {
    private final String goalType;
    private final String target;
    private final String timeframe;

    public GoalsInputData(String goalType, String target, String timeframe) {
        this.goalType = goalType;
        this.target = target;
        this.timeframe = timeframe;
    }

    public String getGoalType() { return goalType; }
    public String getTarget() { return target; }
    public String getTimeframe() { return timeframe; }
}
