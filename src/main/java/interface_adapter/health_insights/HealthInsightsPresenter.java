package interface_adapter.health_insights;

import use_case.health_insights.HealthInsightsOutputBoundary;
import use_case.health_insights.HealthInsightsOutputData;

public class HealthInsightsPresenter implements HealthInsightsOutputBoundary {
    private final HealthInsightsViewModel healthInsightsViewModel;

    public HealthInsightsPresenter(HealthInsightsViewModel healthInsightsViewModel) {
        this.healthInsightsViewModel = healthInsightsViewModel;
    }

    @Override
    public void prepareSuccessView(HealthInsightsOutputData outputData) {
        HealthInsightsState state = healthInsightsViewModel.getState();

        // ONLY set the insights, don't clear error message if it's working
        state.setInsights(outputData.getInsights());

        // Only clear error if there was one before
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            state.setErrorMessage("");
        }

        healthInsightsViewModel.setState(state);

        // Fire property change to notify the view
        healthInsightsViewModel.firePropertyChange();

        System.out.println("Insights generated successfully: " + outputData.getInsights());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        HealthInsightsState state = healthInsightsViewModel.getState();
        state.setErrorMessage(errorMessage);
        // Don't clear insights if they exist
        healthInsightsViewModel.setState(state);

        // Fire property change to notify the view
        healthInsightsViewModel.firePropertyChange();

        System.out.println("Error: " + errorMessage);
    }
}