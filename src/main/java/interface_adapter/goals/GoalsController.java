package interface_adapter.goals;

import use_case.goals.GoalsInputBoundary;
import use_case.goals.GoalsInputData;

public class GoalsController {
    private final GoalsInputBoundary goalsUseCaseInteractor;

    public GoalsController(GoalsInputBoundary goalsUseCaseInteractor) {
        this.goalsUseCaseInteractor = goalsUseCaseInteractor;
    }

    public void execute(String goalType, String target, String timeframe) {
        GoalsInputData inputData = new GoalsInputData(goalType, target, timeframe);
        goalsUseCaseInteractor.execute(inputData);
    }

}
