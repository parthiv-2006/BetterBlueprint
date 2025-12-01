package use_case.settings;

import Entities.User;

/**
 * The Settings Interactor - handles the business logic for updating user settings.
 */
public class SettingsInteractor implements SettingsInputBoundary {

    private final SettingsUserDataAccessInterface userDataAccessObject;
    private final SettingsOutputBoundary settingsPresenter;

    public SettingsInteractor(SettingsUserDataAccessInterface userDataAccessObject,
                              SettingsOutputBoundary settingsPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.settingsPresenter = settingsPresenter;
    }

    @Override
    public void execute(SettingsInputData settingsInputData) {
        // Validate inputs
        if ("".equals(settingsInputData.getAge()) || "".equals(settingsInputData.getHeight())
                || "".equals(settingsInputData.getWeight())) {
            settingsPresenter.prepareFailView("All fields must be filled.");
            return;
        }

        try {
            // Parse and validate the numeric values
            final int age = Integer.parseInt(settingsInputData.getAge());
            final int height = Integer.parseInt(settingsInputData.getHeight());
            final int weight = Integer.parseInt(settingsInputData.getWeight());

            if (age <= 0 || height <= 0 || weight <= 0) {
                settingsPresenter.prepareFailView("All values must be positive numbers.");
                return;
            }

            // Get current user and update their settings
            final String currentUsername = userDataAccessObject.getCurrentUsername();
            final User user = userDataAccessObject.get(currentUsername);
            user.updateAgeHeightWeight(age, height, weight);

            // Save updated user
            userDataAccessObject.save(user);

            // Prepare success response
            final SettingsOutputData outputData = new SettingsOutputData(age, height, weight);
            settingsPresenter.prepareSuccessView(outputData);

        } catch (NumberFormatException exception) {
            settingsPresenter.prepareFailView("Please enter valid numbers for all fields.");
        }
    }
}
