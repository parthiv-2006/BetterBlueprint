package use_case.input_metrics;

/**
 * Output boundary for the Input Metrics use case.
 */
public interface InputMetricsOutputBoundary {
    /**
     * Prepares the success view.
     * @param outputData the output data
     */
    void prepareSuccessView(InputMetricsOutputData outputData);

    /**
     * Prepares the fail view.
     * @param errorMessage the error message
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the home view.
     */
    void switchToHomeView();
}

