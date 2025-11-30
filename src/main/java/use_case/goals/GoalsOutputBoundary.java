package use_case.goals;

public interface GoalsOutputBoundary {
    void present(GoalsOutputData outputData);

    void prepareFailView(String errorMessage);

    void redirectToSettings(String message);

}
