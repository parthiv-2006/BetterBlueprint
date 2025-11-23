package interface_adapter.input_metrics;

import use_case.input_metrics.InputMetricsInputBoundary;
import use_case.input_metrics.InputMetricsInputData;

/**
 * Controller for the Input Metrics use case.
 */
public class InputMetricsController {
    private final InputMetricsInputBoundary inputMetricsInteractor;

    public InputMetricsController(InputMetricsInputBoundary inputMetricsInteractor) {
        this.inputMetricsInteractor = inputMetricsInteractor;
    }

    /**
     * Executes the input metrics use case.
     * @param username the username
     * @param sleepHours sleep hours
     * @param waterIntake water intake in liters
     * @param caloriesConsumed calories consumed
     * @param exerciseDuration exercise duration in minutes
     */
    public void execute(String username, double sleepHours, double waterIntake,
                       int caloriesConsumed, double exerciseDuration) {
        InputMetricsInputData inputData = new InputMetricsInputData(
                username, sleepHours, waterIntake, caloriesConsumed, exerciseDuration);
        inputMetricsInteractor.execute(inputData);
    }

    /**
     * Switches to the home view.
     */
    public void switchToHomeView() {
        inputMetricsInteractor.switchToHomeView();
    }
}


