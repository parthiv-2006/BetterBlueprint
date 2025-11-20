package use_case.health_insights;

public class HealthInsightsInputData {
    private final String userId;

    public HealthInsightsInputData(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
