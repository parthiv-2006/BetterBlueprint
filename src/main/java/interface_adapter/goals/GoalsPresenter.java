package interface_adapter.goals;

import use_case.goals.GoalsOutputBoundary;
import use_case.goals.GoalsOutputData;

/**
 * Presenter for the Goals use case. Converts interactor output into a GoalsState
 * and notifies the GoalsViewModel.
 */
public class GoalsPresenter implements GoalsOutputBoundary {

    private final GoalsViewModel goalsViewModel;

    public GoalsPresenter(GoalsViewModel goalsViewModel) {
        this.goalsViewModel = goalsViewModel;
    }

    @Override
    public void present(GoalsOutputData outputData) {
        GoalsState state = goalsViewModel.getState();

        // Basic info from use case
        state.setGoalType(outputData.getGoalType());
        state.setDailyIntakeCalories(outputData.getDailyIntakeCalories());
        state.setDailyBurnCalories(outputData.getDailyBurnCalories());
        state.setExplanation(outputData.getExplanation());
        state.setErrorMessage(null);

        // Weight info → label for UI
        if (outputData.getCurrentWeightKg() > 0) {
            state.setCurrentWeight(outputData.getCurrentWeightKg());
            state.setCurrentWeightLabel(
                    "Current weight: " + outputData.getCurrentWeightKg() + " kg"
            );
        } else {
            state.setCurrentWeight(0);
            state.setCurrentWeightLabel(
                    "Current weight: not set — open Settings"
            );
        }

        // Target and timeframe
        state.setTarget(outputData.getTarget());
        state.setTimeframe(outputData.getTimeframe());

        // Difference text – all math lives here, not in the View
        try {
            double intake = Double.parseDouble(outputData.getDailyIntakeCalories());
            double burn = Double.parseDouble(outputData.getDailyBurnCalories());
            double diff = intake - burn;

            String diffText;
            if (diff > 0) {
                diffText = "Caloric difference: +" + String.format("%.0f", diff) + " kcal (surplus)";
            } else if (diff < 0) {
                diffText = "Caloric difference: " + String.format("%.0f", diff) + " kcal (deficit)";
            } else {
                diffText = "Caloric difference: 0 kcal (balanced)";
            }
            state.setDifferenceText(diffText);
        } catch (NumberFormatException e) {
            state.setDifferenceText("Caloric difference: -- kcal");
        }

        // Result is ready to show
        state.setResultReady(true);

        // No redirect on success
        state.setShouldRedirectToSettings(false);
        state.setRedirectMessage(null);

        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        GoalsState state = goalsViewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setResultReady(false);
        state.setShouldRedirectToSettings(false);
        state.setRedirectMessage(null);
        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }

    @Override
    public void presentError(String errorMessage) {
        GoalsState state = goalsViewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setResultReady(false);
        state.setShouldRedirectToSettings(false);
        state.setRedirectMessage(null);
        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }

    @Override
    public void redirectToSettings(String message) {
        GoalsState state = goalsViewModel.getState();
        state.setShouldRedirectToSettings(true);
        state.setRedirectMessage(message);
        state.setResultReady(false);
        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }

    @Override
    public void prepareSuccessView(GoalsOutputData outputData) {
        GoalsState state = goalsViewModel.getState();

        state.setGoalType(outputData.getGoalType());
        state.setDailyIntakeCalories(outputData.getDailyIntakeCalories());
        state.setDailyBurnCalories(outputData.getDailyBurnCalories());
        state.setExplanation(outputData.getExplanation());
        state.setErrorMessage(null);
        state.setTimeframe(outputData.getTimeframe());
        state.setTarget(outputData.getTarget());
        state.setResultReady(true);

        if (outputData.getCurrentWeightKg() > 0) {
            state.setCurrentWeightLabel(
                    "Current weight: " + outputData.getCurrentWeightKg() + " kg"
            );
        } else {
            state.setCurrentWeightLabel(
                    "Current weight: not set — open Settings"
            );
        }

        // Calculate difference text – all math lives here, not in the View
        try {
            double intake = Double.parseDouble(outputData.getDailyIntakeCalories());
            double burn = Double.parseDouble(outputData.getDailyBurnCalories());
            double diff = intake - burn;

            String diffText;
            if (diff > 0) {
                diffText = "Caloric difference: +" + String.format("%.0f", diff) + " kcal (surplus)";
            } else if (diff < 0) {
                diffText = "Caloric difference: " + String.format("%.0f", diff) + " kcal (deficit)";
            } else {
                diffText = "Caloric difference: 0 kcal (balanced)";
            }
            state.setDifferenceText(diffText);
        } catch (NumberFormatException e) {
            state.setDifferenceText("Caloric difference: -- kcal");
        }

        state.setShouldRedirectToSettings(outputData.shouldRedirectToSettings());
        state.setRedirectMessage(outputData.getRedirectMessage());

        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }

    @Override
    public void updateCurrentWeight(int weightKg) {
        GoalsState state = goalsViewModel.getState();

        if (weightKg > 0) {
            state.setCurrentWeight(weightKg);
            state.setCurrentWeightLabel("Current weight: " + weightKg + " kg");
        } else {
            state.setCurrentWeight(0);
            state.setCurrentWeightLabel("Current weight: not set — open Settings");
        }

        state.setShouldRedirectToSettings(false);
        state.setRedirectMessage(null);

        goalsViewModel.setState(state);
        goalsViewModel.firePropertyChange();
    }
}
