package interface_adapter.health_history;
import use_case.healthHistory.healthHistoryInputBoundary;

/**
 * The controller for the Health History Use Case.
 */

public class HealthHistoryController {
    private final healthHistoryInputBoundary interactor;

    public HealthHistoryController(healthHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

}