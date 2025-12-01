package interface_adapter.settings;

import interface_adapter.goals.GoalsState;
import interface_adapter.goals.GoalsViewModel;
import use_case.settings.SettingsOutputBoundary;
import use_case.settings.SettingsOutputData;

/**
 * The Presenter for the Settings Use Case.
 *
 * Note: This presenter currently updates both SettingsViewModel and GoalsViewModel.
 * This is a temporary coupling - ideally, GoalsViewModel should observe User changes
 * through an event system or shared state observer pattern.
 */
public class SettingsPresenter implements SettingsOutputBoundary {

    private final SettingsViewModel settingsViewModel;
    private final GoalsViewModel goalsViewModel;

    public SettingsPresenter(SettingsViewModel settingsViewModel,
                             GoalsViewModel goalsViewModel) {
        this.settingsViewModel = settingsViewModel;
        this.goalsViewModel = goalsViewModel;
    }

    @Override
    public void prepareSuccessView(SettingsOutputData outputData) {
        // --- Update SettingsViewModel as before ---
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setAge(String.valueOf(outputData.getAge()));
        settingsState.setHeight(String.valueOf(outputData.getHeight()));
        settingsState.setWeight(String.valueOf(outputData.getWeight()));
        settingsState.setSettingsError(null);


        this.settingsViewModel.firePropertyChange("settingsSaved");
        final GoalsState goalsState = goalsViewModel.getState();
        int w = outputData.getWeight();
        String weightLabel;
        if (w <= 0) {
            weightLabel = "Current weight: not set — open Settings";
        } else {
            weightLabel = "Current weight: " + w + " kg";
        }

        goalsState.setCurrentWeightLabel(weightLabel);
        goalsViewModel.setState(goalsState);
        goalsViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setSettingsError(errorMessage);
        this.settingsViewModel.firePropertyChange();
    }
}
