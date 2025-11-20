package use_case.health_insights;

public interface HealthInsightsOutputBoundary {
    void prepareSuccessView(HealthInsightsOutputData outputData);
    void prepareFailView(String errorMessage);
}
