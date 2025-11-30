package interface_adapter.health_insights;

import use_case.health_insights.HealthInsightsOutputBoundary;
import use_case.health_insights.HealthInsightsOutputData;

import javax.swing.*;

public class HealthInsightsPresenter implements HealthInsightsOutputBoundary {
    private final HealthInsightsViewModel healthInsightsViewModel;

    public HealthInsightsPresenter(HealthInsightsViewModel healthInsightsViewModel) {
        this.healthInsightsViewModel = healthInsightsViewModel;
    }

    @Override
    public void prepareSuccessView(HealthInsightsOutputData outputData) {
        HealthInsightsState state = healthInsightsViewModel.getState();

        SwingUtilities.invokeLater(() -> {
            state.setInsights(outputData.getInsights());

            state.setErrorMessage("");

            healthInsightsViewModel.setState(state);
            healthInsightsViewModel.firePropertyChange();

            System.out.println("Insights generated successfully: " + outputData.getInsights());
        });
    }

    @Override
    public void prepareFailView(String errorMessage) {
        HealthInsightsState state = healthInsightsViewModel.getState();

        SwingUtilities.invokeLater(() -> {
            state.setErrorMessage(errorMessage);
            healthInsightsViewModel.setState(state);
            healthInsightsViewModel.firePropertyChange();

            System.out.println("Error generating insights: " + errorMessage);
        });
    }
}