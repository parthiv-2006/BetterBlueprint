package use_case.healthHistory;

/**
 * Output boundary for presenting the results of the Health Score History use case.
 * Implemented by the Presenter. The Interactor calls these methods.
 */
public interface healthHistoryOutputBoundary {

    /**
     * Called when the use case successfully produces output data.
     * @param outputData the formatted data to be passed to the presenter
     */
    default void prepareSuccessView(healthHistoryOutputData outputData) {
        System.out.println("Success: " + outputData.getMetricType() +
                " (" + outputData.getTimeRange() + ")");
    }

    /**
     * Called when the use case fails.
     * @param errorMessage explanation of the failure
     */
    default void prepareFailView(String errorMessage) {
        System.err.println("HealthScoreHistory Error: " + errorMessage);
    }
}
