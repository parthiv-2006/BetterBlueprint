package interface_adapter.health_history;
import use_case.healthHistory.healthHistoryInputBoundary;
import use_case.healthHistory.healthHistoryInputData;

/**
 * The controller for the Health History Use Case.
 */

public class HealthHistoryController {
    private final healthHistoryInputBoundary interactor;

    public HealthHistoryController(healthHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void fetchHistory(String metricType, String timeRange, String userId) {
        healthHistoryInputData inputData =
                new healthHistoryInputData(metricType, timeRange, userId);

        interactor.execute(inputData);
    }
}