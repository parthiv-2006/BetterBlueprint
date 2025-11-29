package interface_adapter.change_password;

import interface_adapter.ViewManagerModel;
import interface_adapter.settings.SettingsViewModel;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.change_password.ChangePasswordOutputData;

/**
 * The Presenter for the Change Password Use Case.
 */
public class ChangePasswordPresenter implements ChangePasswordOutputBoundary {

    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    public ChangePasswordPresenter(ViewManagerModel viewManagerModel,
                                   SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareSuccessView(ChangePasswordOutputData outputData) {
        settingsViewModel.firePropertyChange("passwordChanged");
    }

    @Override
    public void prepareFailView(String error) {
        settingsViewModel.getState().setPasswordError(error);
        settingsViewModel.firePropertyChange("passwordError");
    }
}
