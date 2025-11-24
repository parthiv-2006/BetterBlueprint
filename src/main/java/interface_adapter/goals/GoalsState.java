package interface_adapter.goals;

public class GoalsState {
    private String goalType;
    private String targetWeight;
    private String timeframe;
    private String errorMessage;
    private String planText;      // where we'll show the AI plan later

    public GoalsState() {}

    public String getGoalType() { return goalType; }
    public void setGoalType(String goalType) { this.goalType = goalType; }

    public String getTargetWeight() { return targetWeight; }
    public void setTargetWeight(String targetWeight) { this.targetWeight = targetWeight; }

    public String getTimeframe() { return timeframe; }
    public void setTimeframe(String timeframe) { this.timeframe = timeframe; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getPlanText() { return planText; }
    public void setPlanText(String planText) { this.planText = planText; }
}
