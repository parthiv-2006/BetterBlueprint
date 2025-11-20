package use_case.health_insights;

public class HealthInsightsOutputData {
    private final String insights;

    public HealthInsightsOutputData(String insights) {
        this.insights = insights;
    }

    public String getInsights() {
        return insights;
    }
}
