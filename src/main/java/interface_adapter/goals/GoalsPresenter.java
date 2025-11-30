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

        state.setGoalType(outputData.getGoalType());
        state.setDailyIntakeCalories(outputData.getDailyIntakeCalories());
        state.setDailyBurnCalories(outputData.getDailyBurnCalories());
        state.setExplanation(outputData.getExplanation());
        state.setCurrentWeight(outputData.getCurrentWeightKg());
        state.setErrorMessage(null);

        // Current weight label
        if (outputData.getCurrentWeightKg() > 0) {
            state.setCurrentWeightLabel(
                    "Current weight: " + outputData.getCurrentWeightKg() + " kg"
            );
        } else {
            state.setCurrentWeightLabel(
                    "Current weight: not set — open Settings"
            );
        }

        // Redirect flags
        state.setShouldRedirectToSettings(outputData.shouldRedirectToSettings());
        state.setRedirectMessage(outputData.getRedirectMessage());

        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }

    @Override
    public void redirectToSettings(String message) {
        GoalsState state = goalsViewModel.getState();
        state.setShouldRedirectToSettings(true);
        state.setRedirectMessage(message);

        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }

    public void prepareFailView(String errorMessage) {
        GoalsState state = goalsViewModel.getState();
        state.setErrorMessage(errorMessage);
        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }
}


