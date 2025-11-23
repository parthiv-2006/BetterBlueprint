package interface_adapter.input_metrics;

import interface_adapter.ViewManagerModel;
import use_case.input_metrics.InputMetricsOutputBoundary;
import use_case.input_metrics.InputMetricsOutputData;

/**
 * The Presenter for the Input Metrics use case.
 */
public class InputMetricsPresenter implements InputMetricsOutputBoundary {
    private final InputMetricsViewModel inputMetricsViewModel;
    private final ViewManagerModel viewManagerModel;

    public InputMetricsPresenter(InputMetricsViewModel inputMetricsViewModel,
                                 ViewManagerModel viewManagerModel) {
        this.inputMetricsViewModel = inputMetricsViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(InputMetricsOutputData outputData) {
        // Clear the input fields on success
        InputMetricsState state = inputMetricsViewModel.getState();
        state.setSleepHours("");
        state.setWaterIntake("");
        state.setCaloriesConsumed("");
        state.setExerciseDuration("");
        state.setErrorMessage(null);

        inputMetricsViewModel.setState(state);
        inputMetricsViewModel.firePropertyChange();

        // Show success message (you might want to add a success message field to state)
        state.setErrorMessage("Metrics saved successfully for " + outputData.getDate());
        inputMetricsViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        InputMetricsState state = inputMetricsViewModel.getState();
        state.setErrorMessage(errorMessage);
        inputMetricsViewModel.firePropertyChange();
    }

    @Override
    public void switchToHomeView() {
        viewManagerModel.setState("home");
        viewManagerModel.firePropertyChange();
    }
}

