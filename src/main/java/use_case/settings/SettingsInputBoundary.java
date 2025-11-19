package use_case.settings;

/**
 * Input Boundary for actions related to user settings.
 */
public interface SettingsInputBoundary {

    /**
     * Executes the settings use case.
     * @param settingsInputData the input data
     */
    void execute(SettingsInputData settingsInputData);

    /**
     * Switches back to the Home view.
     */
    void switchToHomeView();
}
