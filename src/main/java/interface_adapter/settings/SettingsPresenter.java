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

        // Generate dynamic success message based on what was updated
        String successMessage = buildSuccessMessage(
            outputData.isAgeUpdated(),
            outputData.isHeightUpdated(),
            outputData.isWeightUpdated()
        );
        settingsState.setSuccessMessage(successMessage);

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

    /**
     * Builds a specific success message based on which fields were updated.
     * @param ageUpdated whether age was updated
     * @param heightUpdated whether height was updated
     * @param weightUpdated whether weight was updated
     * @return a user-friendly success message
     */
    private String buildSuccessMessage(boolean ageUpdated, boolean heightUpdated, boolean weightUpdated) {
        java.util.List<String> updatedFields = new java.util.ArrayList<>();
        if (ageUpdated) updatedFields.add("Age");
        if (heightUpdated) updatedFields.add("Height");
        if (weightUpdated) updatedFields.add("Weight");

        if (updatedFields.size() == 3) {
            return "Age, Height, and Weight saved successfully!";
        } else if (updatedFields.size() == 2) {
            return updatedFields.get(0) + " and " + updatedFields.get(1) + " saved successfully!";
        } else {
            return updatedFields.get(0) + " saved successfully!";
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setSettingsError(errorMessage);
        this.settingsViewModel.firePropertyChange();
    }
}
