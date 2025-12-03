package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel extends ViewModel<LoginState> {

    public static final String SIGN_UP_BUTTON_LABEL = "Sign Up";
    public static final String LOG_IN_BUTTON_LABEL = "Log In";

    public LoginViewModel() {
        super("log in");
        setState(new LoginState());
    }

    @Override
    public void reset() {
        setState(new LoginState());
        firePropertyChange();
    }
}
