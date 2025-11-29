package app;

import Entities.User;
import Entities.UserFactory;
import data_access.DailyHealthScoreDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.HealthMetricsDataAccessObject;
import Entities.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.ChangePasswordPresenter;
import interface_adapter.daily_health_score.*;
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
//import interface_adapter.logout.LogoutController;
//import interface_adapter.logout.LogoutPresenter;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.daily_health_score.*;
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
//import use_case.logout.LogoutInputBoundary;
//import use_case.logout.LogoutInteractor;
//import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
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


        // Add to card panel
        cardPanel.add(healthInsightsView, healthInsightsView.viewName);

        return this;
    }

    // In your AppBuilder.java, make sure you're creating HealthInsightsView AFTER the controller is created
    public AppBuilder addHomeView() {
        // Create InputMetricsView first
        inputMetricsViewModel = new InputMetricsViewModel();
        inputMetricsView = new InputMetricsView(inputMetricsViewModel);

        // Create GoalsView
        goalsViewModel = new GoalsViewModel();
        goalsView = new GoalsView(goalsViewModel);

        // Create HomeView and pass actual views
        homeViewModel = new HomeViewModel();
        homeView = new HomeView(homeViewModel, viewManagerModel, inputMetricsView, settingsViewModel, myScoreView, healthInsightsView, goalsView);

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
        HealthScoreCalculator scoreCalculator;
        if (apiKey != null && !apiKey.isEmpty()) {
            scoreCalculator = new GeminiHealthScoreCalculator(apiKey);
        } else {
            // Use simple fallback calculator when API key is not available
            scoreCalculator = new SimpleHealthScoreCalculator();
        }

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


    public AppBuilder addGoalsUseCase() {

        // Try to get the currently logged-in username (may be null)
        String currentUsername = userDataAccessObject.getCurrentUsername();

        User entityUser = null;
        if (currentUsername != null) {
            entityUser = userDataAccessObject.get(currentUsername);
        }

        // --- default values if we know nothing about the user yet ---
        String name = "Guest";
        String password = "default";
        int age = 30;
        int height = 170;
        int weight = 70;

        // If we found a stored user, use their name/password
        if (entityUser != null) {
            name = entityUser.getName();
            password = entityUser.getPassword();

            // Try to pull latest health metrics (may be null if never entered)
            var latestMetrics = healthMetricsDataAccessObject.getLatestMetrics(currentUsername);

            if (latestMetrics != null) {
                age = latestMetrics.getAge();
                height = latestMetrics.getHeight();
                weight = latestMetrics.getWeight();
            }
        }

        // Build the User object that the goals use case will use
        User currentUser = new User(name, password, age, height, weight);

        // Wire up Goals use case
        GoalsPresenter goalsPresenter = new GoalsPresenter(goalsViewModel);
        GoalsInteractor goalsInteractor = new GoalsInteractor(goalsPresenter, currentUser);
        GoalsController goalsController = new GoalsController(goalsInteractor);

        goalsView.setGoalsController(goalsController);

        return this;
    }

}