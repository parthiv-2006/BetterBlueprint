package use_case.daily_health_score;

/**
 * The Output Boundary for the Daily Health Score Use Case.
 */

public interface DailyHealthScoreOutputBoundary {
    /**
     * Prepares the success view for the Change Password Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(DailyHealthScoreOutputData outputData);

    /**
     * Prepares the failure view for the Change Password Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}