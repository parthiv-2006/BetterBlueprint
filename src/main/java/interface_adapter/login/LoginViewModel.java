package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel extends ViewModel<LoginState> {

    public static final String CANCEL_BUTTON_LABEL = "Cancel";
    public static final String LOG_IN_BUTTON_LABEL = "Log In";

    public LoginViewModel() {
        super("log in");
        setState(new LoginState());
    }

}
