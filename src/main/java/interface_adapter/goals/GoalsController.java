package interface_adapter.goals;

import use_case.goals.GoalsInputBoundary;
import use_case.goals.GoalsInputData;

/**
 * Controller for Goals UI events. Converts view actions into use case input.
 */
public class GoalsController {

    private final GoalsInputBoundary interactor;

    public GoalsController(GoalsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String goalType, String target, String timeframe) {
        GoalsInputData inputData = new GoalsInputData(goalType, target, timeframe);
        interactor.execute(inputData);
    }

    public void refreshCurrentWeight() {
        interactor.refreshCurrentWeight();
    }
}
