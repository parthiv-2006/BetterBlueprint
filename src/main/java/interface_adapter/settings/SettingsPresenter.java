package interface_adapter.settings;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;
import use_case.settings.SettingsOutputBoundary;
import use_case.settings.SettingsOutputData;

/**
 * The Presenter for the Settings Use Case.
 */
public class SettingsPresenter implements SettingsOutputBoundary {

    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;
    private final HomeViewModel homeViewModel;

    public SettingsPresenter(ViewManagerModel viewManagerModel,
                             SettingsViewModel settingsViewModel,
                             HomeViewModel homeViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
        this.homeViewModel = homeViewModel;
    }

    @Override
    public void prepareSuccessView(SettingsOutputData outputData) {
        // Update the settings state to reflect the saved values
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setAge(String.valueOf(outputData.getAge()));
        settingsState.setHeight(String.valueOf(outputData.getHeight()));
        settingsState.setWeight(String.valueOf(outputData.getWeight()));
        settingsState.setSettingsError(null);
        this.settingsViewModel.firePropertyChange();

        // Switch back to Home view after successful save
        this.viewManagerModel.setState(homeViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setSettingsError(errorMessage);
        this.settingsViewModel.firePropertyChange();
    }

    @Override
    public void switchToHomeView() {
        this.viewManagerModel.setState(homeViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }
}
