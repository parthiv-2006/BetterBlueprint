package use_case.input_metrics;

/**
 * Input boundary for the Input Metrics use case.
 */
public interface InputMetricsInputBoundary {
    /**
     * Executes the input metrics use case.
     * @param inputMetricsInputData the input data
     */
    void execute(InputMetricsInputData inputMetricsInputData);

    /**
     * Switches to the home view.
     */
    void switchToHomeView();
}

