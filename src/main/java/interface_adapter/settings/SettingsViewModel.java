package interface_adapter.settings;

import interface_adapter.ViewModel;

/**
 * The View Model for the Settings View.
 */
public class SettingsViewModel extends ViewModel<SettingsState> {

    public static final String TITLE_LABEL = "Settings";
    public static final String AGE_LABEL = "Age";
    public static final String HEIGHT_LABEL = "Height (cm)";
    public static final String WEIGHT_LABEL = "Weight (kg)";
    public static final String SAVE_BUTTON_LABEL = "Save";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public SettingsViewModel() {
        super("settings");
        setState(new SettingsState());
    }

    @Override
    public void reset() {
        setState(new SettingsState());
        firePropertyChange();
    }
}
