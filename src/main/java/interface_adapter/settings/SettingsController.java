package interface_adapter.settings;

import use_case.settings.SettingsInputBoundary;
import use_case.settings.SettingsInputData;

/**
 * The controller for the Settings Use Case.
 */
public class SettingsController {

    private final SettingsInputBoundary settingsUseCaseInteractor;

    public SettingsController(SettingsInputBoundary settingsUseCaseInteractor) {
        this.settingsUseCaseInteractor = settingsUseCaseInteractor;
    }

    /**
     * Executes the Settings Use Case.
     * @param age the user's age
     * @param height the user's height in cm
     * @param weight the user's weight in kg
     */
    public void execute(String age, String height, String weight) {
        final SettingsInputData settingsInputData = new SettingsInputData(age, height, weight);
        settingsUseCaseInteractor.execute(settingsInputData);
    }

    /**
     * Switches back to the Home View.
     */
    public void switchToHomeView() {
        settingsUseCaseInteractor.switchToHomeView();
    }
}
