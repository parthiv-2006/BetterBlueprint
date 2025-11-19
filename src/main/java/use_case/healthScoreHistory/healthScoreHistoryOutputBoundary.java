package use_case.healthScoreHistory;

import use_case.healthScoreHistory.healthScoreHistoryOutputData;

public interface healthScoreHistoryOutputBoundary {
    /**
     * Prepares the success view for the Login Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(healthScoreHistoryOutputData outputData);
    /**
     * Prepares the failure view for the Login Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
