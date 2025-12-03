package interface_adapter.home;

import interface_adapter.ViewModel;

public class HomeViewModel extends ViewModel<HomeState> {
//    public static final String LOG_OUT_BUTTON_LABEL = "Log Out";

    public HomeViewModel() {
        super("home");
        setState(new HomeState());
    }

    @Override
    public void reset() {
        setState(new HomeState());
        firePropertyChange();
    }
}
