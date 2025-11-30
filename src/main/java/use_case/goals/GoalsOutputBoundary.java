package use_case.goals;

public interface GoalsOutputBoundary {
    void present(GoalsOutputData outputData);

    void prepareFailView(String errorMessage);

    void presentError(String errorMessage);

    void redirectToSettings(String message);

    void prepareSuccessView(GoalsOutputData outputData);

    void updateCurrentWeight(int weightKg);
}
