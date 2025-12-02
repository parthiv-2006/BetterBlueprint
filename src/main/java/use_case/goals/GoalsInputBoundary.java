package use_case.goals;

/**
 * Input boundary for the Goals use case.
 * Receives user input from controllers and forwards to the interactor.
 */
public interface GoalsInputBoundary {
    void execute(GoalsInputData inputData);

    void refreshCurrentWeight();
}
