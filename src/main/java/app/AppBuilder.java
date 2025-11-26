package app;

import data_access.DailyHealthScoreDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.HealthMetricsDataAccessObject;
import Entities.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.ChangePasswordPresenter;
import interface_adapter.daily_health_score.DailyHealthScoreController;
import interface_adapter.daily_health_score.DailyHealthScorePresenter;
import interface_adapter.daily_health_score.DailyHealthScoreViewModel;
import interface_adapter.daily_health_score.GeminiHealthScoreCalculator;
import interface_adapter.health_insights.HealthInsightsController;
import interface_adapter.health_insights.HealthInsightsPresenter;
import interface_adapter.health_insights.HealthInsightsViewModel;
import interface_adapter.home.HomeViewModel;
import interface_adapter.input_metrics.InputMetricsController;
import interface_adapter.input_metrics.InputMetricsPresenter;
import interface_adapter.input_metrics.InputMetricsViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.daily_health_score.*;
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
    // DAO version using local file storage
    private final HealthMetricsDataAccessObject healthMetricsDataAccessObject = new HealthMetricsDataAccessObject(userDataAccessObject);

    // DAO version using a shared external database

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

    public AppBuilder addSettingsView() {
        settingsViewModel = new SettingsViewModel();
        settingsView = new SettingsView(settingsViewModel);
        cardPanel.add(settingsView, settingsView.getViewName());
        return this;
    }

    public AppBuilder addHealthInsightsUseCase() {
        // Create Health Insights components
        healthInsightsViewModel = new HealthInsightsViewModel();

        // Create the service
        GeminiAPIService geminiAPIService = new GeminiAPIService();

        // Create the interactor
        HealthInsightsOutputBoundary healthInsightsOutputBoundary =
                new HealthInsightsPresenter(healthInsightsViewModel);

        HealthInsightsInputBoundary healthInsightsInteractor =
                new HealthInsightsInteractor(
                        healthInsightsOutputBoundary,
                        healthMetricsDataAccessObject,
                        userDataAccessObject,
                        geminiAPIService
                );

        // Create the controller
        healthInsightsController = new HealthInsightsController(healthInsightsInteractor);

        // Create the view
        healthInsightsView = new HealthInsightsView(healthInsightsViewModel, healthInsightsController);

        // IMPORTANT: Inject the controller into the view
        healthInsightsView.setHealthInsightsController(healthInsightsController);

        // Add to card panel
        cardPanel.add(healthInsightsView, healthInsightsView.viewName);

        return this;
    }

    public AppBuilder addHomeView() {
        // Create InputMetricsView first
        inputMetricsViewModel = new InputMetricsViewModel();
        inputMetricsView = new InputMetricsView(inputMetricsViewModel);

        // Create HealthInsightsView WITH the controller
        healthInsightsViewModel = new HealthInsightsViewModel();
        healthInsightsView = new HealthInsightsView(healthInsightsViewModel, healthInsightsController); // Pass the controller

        // Create HomeView and pass actual views
        homeViewModel = new HomeViewModel();
        homeView = new HomeView(homeViewModel, viewManagerModel, inputMetricsView, settingsViewModel, myScoreView, healthInsightsView);
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
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                homeViewModel, loginViewModel, signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

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
        final SettingsOutputBoundary settingsOutputBoundary = new SettingsPresenter(viewManagerModel,
                settingsViewModel, homeViewModel);
        final SettingsInputBoundary settingsInteractor = new SettingsInteractor(
                userDataAccessObject, settingsOutputBoundary);

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

        // Read API key from environment variable
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("GEMINI_API_KEY environment variable is not set. " +
                    "Please set it before running the application.");
        }
        HealthScoreCalculator scoreCalculator = new GeminiHealthScoreCalculator(apiKey);

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