package interface_adapter.health_insights;

import use_case.health_insights.HealthInsightsInputBoundary;
import use_case.health_insights.HealthInsightsInputData;

public class HealthInsightsController {
    private final HealthInsightsInputBoundary healthInsightsUseCase;

    public HealthInsightsController(HealthInsightsInputBoundary healthInsightsUseCase) {
        this.healthInsightsUseCase = healthInsightsUseCase;
    }

    public void execute(String userId) {
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);
        healthInsightsUseCase.execute(inputData);
    }
}
