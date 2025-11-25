package interface_adapter.goals;

import use_case.goals.GoalsOutputBoundary;
import use_case.goals.GoalsOutputData;

public class GoalsPresenter implements GoalsOutputBoundary {

    private final GoalsViewModel goalsViewModel;

    public GoalsPresenter(GoalsViewModel goalsViewModel) {
        this.goalsViewModel = goalsViewModel;
    }

    @Override
    public void present(GoalsOutputData outputData) {
        GoalsState state = goalsViewModel.getState();

        state.setDailyIntakeCalories(outputData.getDailyIntakeCalories());
        state.setDailyBurnCalories(outputData.getDailyBurnCalories());
        state.setExplanation(outputData.getExplanation());
        state.setErrorMessage(null);   // clear any previous error if you have this method

        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }
}
