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
        state.setInsights(outputData.getInsights());
        state.setErrorMessage("");
        healthInsightsViewModel.setState(state);

        // For now, just print success - you'll integrate navigation later
        System.out.println("Insights generated successfully");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        HealthInsightsState state = healthInsightsViewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setInsights("");
        healthInsightsViewModel.setState(state);

        System.out.println("Error: " + errorMessage);
    }
}