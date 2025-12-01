package app;

import Entities.User;
import Entities.UserFactory;
import data_access.DailyHealthScoreDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.HealthMetricsDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.ChangePasswordPresenter;
import interface_adapter.daily_health_score.*;
import interface_adapter.goals.GoalsState;
import interface_adapter.health_insights.HealthInsightsController;
import interface_adapter.health_insights.HealthInsightsPresenter;
import interface_adapter.health_insights.HealthInsightsViewModel;
import interface_adapter.goals.GoalsPresenter;
import interface_adapter.home.HomeViewModel;
import interface_adapter.input_metrics.InputMetricsController;
import interface_adapter.input_metrics.InputMetricsPresenter;
import interface_adapter.input_metrics.InputMetricsViewModel;
import interface_adapter.goals.GoalsViewModel;
import interface_adapter.goals.GoalsController;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.daily_health_score.*;
import use_case.goals.GoalsInputBoundary;
import use_case.goals.GoalsInteractor;
import use_case.input_metrics.InputMetricsInputBoundary;
import use_case.input_metrics.InputMetricsInteractor;
import use_case.input_metrics.InputMetricsOutputBoundary;
import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsPresenter;
import interface_adapter.settings.SettingsViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.health_insights.HealthInsightsInputBoundary;
import use_case.health_insights.HealthInsightsInteractor;
import use_case.health_insights.HealthInsightsOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.settings.SettingsInputBoundary;
import use_case.settings.SettingsInteractor;
import use_case.settings.SettingsOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.HomeView;
import view.InputMetricsView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;
import view.GoalsView;
import services.GeminiAPIService;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final UserFactory userFactory = new UserFactory();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);
    private final HealthMetricsDataAccessObject healthMetricsDataAccessObject = new HealthMetricsDataAccessObject(userDataAccessObject);


    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private HomeViewModel homeViewModel;
    private HomeView homeView;
    private LoginView loginView;
    private InputMetricsView inputMetricsView;
    private InputMetricsViewModel inputMetricsViewModel;
    private SettingsView settingsView;
    private SettingsViewModel settingsViewModel;
    private DailyHealthScoreViewModel dailyHealthScoreViewModel;
    private MyScoreView myScoreView;
    private DailyHealthScoreController dailyHealthScoreController;
    private HealthInsightsView healthInsightsView;
    private HealthInsightsViewModel healthInsightsViewModel;
    private HealthInsightsController healthInsightsController;
    private GoalsView goalsView;
    private GoalsViewModel goalsViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addSettingsViewModel() {
        settingsViewModel = new SettingsViewModel();
        return this;
    }

    public AppBuilder addSettingsView() {
        settingsView = new SettingsView(settingsViewModel, viewManagerModel, homeViewModel);
        cardPanel.add(settingsView, settingsView.getViewName());
        return this;
    }

    public AppBuilder addHealthInsightsUseCase() {
        healthInsightsViewModel = new HealthInsightsViewModel();

        GeminiAPIService geminiAPIService = new GeminiAPIService();

        HealthInsightsOutputBoundary healthInsightsOutputBoundary =
                new HealthInsightsPresenter(healthInsightsViewModel);

        HealthInsightsInputBoundary healthInsightsInteractor =
                new HealthInsightsInteractor(
                        healthInsightsOutputBoundary,
                        healthMetricsDataAccessObject,
                        userDataAccessObject,
                        geminiAPIService
                );

        healthInsightsController = new HealthInsightsController(healthInsightsInteractor);

        healthInsightsView = new HealthInsightsView(healthInsightsViewModel, healthInsightsController);


        cardPanel.add(healthInsightsView, healthInsightsView.viewName);

        return this;
    }

    public AppBuilder addHomeView() {
        inputMetricsViewModel = new InputMetricsViewModel();
        inputMetricsView = new InputMetricsView(inputMetricsViewModel);

        // Create GoalsView
        goalsViewModel = new GoalsViewModel();
        goalsView = new GoalsView(goalsViewModel);

        homeViewModel = new HomeViewModel();
        homeView = new HomeView(
                homeViewModel,
                viewManagerModel,
                inputMetricsView,
                settingsViewModel,
                myScoreView,
                healthInsightsView,
                goalsView
        );

        cardPanel.add(homeView, homeView.getViewName());

        return this;
    }



    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        final SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(
                viewManagerModel,
                homeViewModel,
                loginViewModel,
                signupViewModel
        );
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary
        );

        final LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);

        return this;
    }


    public AppBuilder addInputMetricsUseCase() {
        final InputMetricsOutputBoundary inputMetricsOutputBoundary =
                new InputMetricsPresenter(inputMetricsViewModel, viewManagerModel);
        final InputMetricsInputBoundary inputMetricsInteractor =
                new InputMetricsInteractor(healthMetricsDataAccessObject, inputMetricsOutputBoundary);

        InputMetricsController controller = new InputMetricsController(inputMetricsInteractor);
        inputMetricsView.setInputMetricsController(controller);
        return this;
    }

    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                homeViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor = new LogoutInteractor(
                userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        settingsView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addSettingsUseCase() {
        final SettingsOutputBoundary settingsOutputBoundary = new SettingsPresenter(
                settingsViewModel,
                goalsViewModel
        );
        final SettingsInputBoundary settingsInteractor = new SettingsInteractor(
                userDataAccessObject,
                settingsOutputBoundary
        );

        final SettingsController settingsController = new SettingsController(settingsInteractor);
        settingsView.setSettingsController(settingsController);

        return this;
    }


    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary =
                new ChangePasswordPresenter(viewManagerModel, settingsViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController =
                new ChangePasswordController(changePasswordInteractor);

        settingsView.setChangePasswordController(changePasswordController);
        return this;
    }

    public AppBuilder addDailyHealthScoreUseCase() {
        // Create ViewModel only once
        dailyHealthScoreViewModel = new DailyHealthScoreViewModel();

        // Create the View once
        myScoreView = new MyScoreView(dailyHealthScoreViewModel, null);

        // Create GeminiAPIService (it reads API key from environment variable)
        GeminiAPIService geminiService = new GeminiAPIService();

        // Create the adapter that delegates to the service (includes built-in fallback)
        HealthScoreCalculator scoreCalculator = new GeminiHealthScoreCalculator(geminiService);

        DailyHealthScoreUserDataAccessInterface metricsDAO =
                new DailyHealthScoreDataAccessObject(userDataAccessObject);

        DailyHealthScoreOutputBoundary presenter =
                new DailyHealthScorePresenter(dailyHealthScoreViewModel);

        DailyHealthScoreInputBoundary interactor =
                new DailyHealthScoreInteractor(metricsDAO, presenter, scoreCalculator);

        dailyHealthScoreController = new DailyHealthScoreController(interactor, metricsDAO);

        // NOW inject controller
        myScoreView.setController(dailyHealthScoreController);

        return this;
    }

    public AppBuilder addGoalsUseCase() {
        GoalsPresenter goalsPresenter = new GoalsPresenter(goalsViewModel);
        GoalsInputBoundary goalsInteractor =
                new GoalsInteractor(userDataAccessObject, goalsPresenter);
        GoalsController goalsController = new GoalsController(goalsInteractor);
        goalsView.setGoalsController(goalsController);

        String currentUsername = userDataAccessObject.getCurrentUsername();
        if (currentUsername != null) {
            User user = userDataAccessObject.get(currentUsername);
            if (user != null) {
                GoalsState state = goalsViewModel.getState();
                state.setCurrentWeight(user.getWeight());
                if (user.getWeight() > 0) {
                    state.setCurrentWeightLabel("Current weight: " + user.getWeight() + " kg");
                } else {
                    state.setCurrentWeightLabel("Current weight: not set — open Settings");
                }
                goalsViewModel.setState(state);
                goalsViewModel.firePropertyChange();
            }
        } else {
            GoalsState state = goalsViewModel.getState();
            state.setCurrentWeightLabel("Current weight: not set — open Settings");
            state.setCurrentWeight(0);
            goalsViewModel.setState(state);
            goalsViewModel.firePropertyChange();
        }

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("BetterBlueprint Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // CHANGE BACK: Set the initial state to signup view (not login)
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }

 }