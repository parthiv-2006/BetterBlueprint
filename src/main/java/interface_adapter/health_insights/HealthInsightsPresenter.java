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
        // Update state on EDT to ensure UI consistency
        javax.swing.SwingUtilities.invokeLater(() -> {
            HealthInsightsState state = healthInsightsViewModel.getState();

            // Set the insights and clear any previous error
            state.setInsights(outputData.getInsights());
            state.setErrorMessage("");

            healthInsightsViewModel.setState(state);
            healthInsightsViewModel.firePropertyChange(); // Fire general property change

            System.out.println("Insights generated successfully: " + outputData.getInsights());
        });
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Update state on EDT to ensure UI consistency
        javax.swing.SwingUtilities.invokeLater(() -> {
            HealthInsightsState state = healthInsightsViewModel.getState();
            state.setErrorMessage(errorMessage);
            // Don't clear insights if they exist
            healthInsightsViewModel.setState(state);
            healthInsightsViewModel.firePropertyChange(); // Fire general property change

            System.out.println("Error: " + errorMessage);
        });
    }
}