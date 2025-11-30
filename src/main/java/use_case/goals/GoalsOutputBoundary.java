package use_case.goals;

public interface GoalsOutputBoundary {
    void present(GoalsOutputData outputData);

    void redirectToSettings(String message);

}
