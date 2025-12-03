package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeState;
import interface_adapter.home.HomeViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * The Presenter for the Logout Use Case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private final HomeViewModel homeViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;

    public LogoutPresenter(ViewManagerModel viewManagerModel,
                           HomeViewModel homeViewModel,
                           LoginViewModel loginViewModel) {
        this.homeViewModel = homeViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData response) {
        // Clear home state
        final HomeState homeState = homeViewModel.getState();
        homeState.setUsername("");
        this.homeViewModel.firePropertyChange();

        // Clear login state
        final LoginState loginState = loginViewModel.getState();
        loginState.setUsername(response.getUsername());
        loginState.setPassword("");
        this.loginViewModel.firePropertyChange();


        // Switch to login view
        this.viewManagerModel.setState(loginViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }
}
