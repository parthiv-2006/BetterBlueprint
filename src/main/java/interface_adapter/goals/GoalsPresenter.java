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

        // Normal successful result: clear any previous redirect
        state.setShouldRedirectToSettings(false);
        state.setRedirectMessage(null);

        state.setDailyIntakeCalories(outputData.getDailyIntakeCalories());
        state.setDailyBurnCalories(outputData.getDailyBurnCalories());
        state.setExplanation(outputData.getExplanation());
        state.setErrorMessage(null);   // clear any previous error

        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }

    @Override
    public void redirectToSettings(String message) {
        GoalsState state = goalsViewModel.getState();

        // Tell the view it should redirect to Settings
        state.setShouldRedirectToSettings(true);
        state.setRedirectMessage(message);
        state.setErrorMessage(null);   // optional: clear old error

        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }
}
