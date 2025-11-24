package interface_adapter.health_insights;

public class HealthInsightsState {
    private String insights = "";
    private String errorMessage = "";

    public String getInsights() { return insights; }
    public void setInsights(String insights) { this.insights = insights; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
