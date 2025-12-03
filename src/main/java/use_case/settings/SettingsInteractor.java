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
        // Check if at least one field is filled
        final boolean ageProvided = settingsInputData.getAge() != null && !settingsInputData.getAge().trim().isEmpty();
        final boolean heightProvided = settingsInputData.getHeight() != null && !settingsInputData.getHeight().trim().isEmpty();
        final boolean weightProvided = settingsInputData.getWeight() != null && !settingsInputData.getWeight().trim().isEmpty();

        if (!ageProvided && !heightProvided && !weightProvided) {
            settingsPresenter.prepareFailView("Please provide at least one field to update.");
            return;
        }

        try {
            // Get current user
            final String currentUsername = userDataAccessObject.getCurrentUsername();
            final User user = userDataAccessObject.get(currentUsername);

            // Update age if provided
            if (ageProvided) {
                final int age = Integer.parseInt(settingsInputData.getAge().trim());
                user.updateAge(age);
            }

            // Update height if provided
            if (heightProvided) {
                final int height = Integer.parseInt(settingsInputData.getHeight().trim());
                user.updateHeight(height);
            }

            // Update weight if provided
            if (weightProvided) {
                final int weight = Integer.parseInt(settingsInputData.getWeight().trim());
                user.updateWeight(weight);
            }

            // Save updated user
            userDataAccessObject.save(user);

            // Prepare success response with current values and update flags
            final SettingsOutputData outputData = new SettingsOutputData(
                user.getAge(),
                user.getHeight(),
                user.getWeight(),
                ageProvided,
                heightProvided,
                weightProvided
            );
            settingsPresenter.prepareSuccessView(outputData);

        } catch (NumberFormatException exception) {
            settingsPresenter.prepareFailView("Please enter valid numbers for the provided fields.");
        } catch (IllegalArgumentException exception) {
            settingsPresenter.prepareFailView(exception.getMessage());
        }
    }
}
