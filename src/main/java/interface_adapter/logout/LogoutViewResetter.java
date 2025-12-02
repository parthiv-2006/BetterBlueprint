package interface_adapter.logout;

import interface_adapter.daily_health_score.DailyHealthScoreState;
import interface_adapter.daily_health_score.DailyHealthScoreViewModel;
import interface_adapter.goals.GoalsState;
import interface_adapter.goals.GoalsViewModel;
import interface_adapter.health_insights.HealthInsightsState;
import interface_adapter.health_insights.HealthInsightsViewModel;
import interface_adapter.input_metrics.InputMetricsState;
import interface_adapter.input_metrics.InputMetricsViewModel;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;

/**
 * Handles resetting all user-specific ViewModels when a user logs out.
 * This ensures that when a new user logs in, they see a clean UI state.
 */
public class LogoutViewResetter {
    private final InputMetricsViewModel inputMetricsViewModel;
    private final GoalsViewModel goalsViewModel;
    private final SettingsViewModel settingsViewModel;
    private final DailyHealthScoreViewModel dailyHealthScoreViewModel;
    private final HealthInsightsViewModel healthInsightsViewModel;

    public LogoutViewResetter(InputMetricsViewModel inputMetricsViewModel,
                              GoalsViewModel goalsViewModel,
                              SettingsViewModel settingsViewModel,
                              DailyHealthScoreViewModel dailyHealthScoreViewModel,
                              HealthInsightsViewModel healthInsightsViewModel) {
        this.inputMetricsViewModel = inputMetricsViewModel;
        this.goalsViewModel = goalsViewModel;
        this.settingsViewModel = settingsViewModel;
        this.dailyHealthScoreViewModel = dailyHealthScoreViewModel;
        this.healthInsightsViewModel = healthInsightsViewModel;
    }

    /**
     * Resets all user-specific ViewModels to their initial empty state.
     */
    public void resetAllViews() {
        if (inputMetricsViewModel != null) {
            inputMetricsViewModel.setState(new InputMetricsState());
            inputMetricsViewModel.firePropertyChange();
        }
        if (goalsViewModel != null) {
            goalsViewModel.setState(new GoalsState());
            goalsViewModel.firePropertyChange();
        }
        if (settingsViewModel != null) {
            settingsViewModel.setState(new SettingsState());
            settingsViewModel.firePropertyChange();
        }
        if (dailyHealthScoreViewModel != null) {
            dailyHealthScoreViewModel.setState(new DailyHealthScoreState());
            dailyHealthScoreViewModel.firePropertyChanged();
        }
        if (healthInsightsViewModel != null) {
            healthInsightsViewModel.setState(new HealthInsightsState());
            healthInsightsViewModel.firePropertyChange();
        }
    }
}

