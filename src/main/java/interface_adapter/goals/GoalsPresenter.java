package interface_adapter.goals;

import use_case.goals.GoalsOutputBoundary;
import use_case.goals.GoalsOutputData;

public class GoalsPresenter implements GoalsOutputBoundary {
    private final GoalsViewModel viewModel;

    public GoalsPresenter(GoalsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GoalsOutputData outputData) {
        GoalsState state = viewModel.getState();
        state.setDailyIntakeCalories(outputData.getDailyIntakeCalories());
        state.setDailyBurnCalories(outputData.getDailyBurnCalories());
        state.setExplanation(outputData.getExplanation());
        viewModel.setState(state);
    }
}
