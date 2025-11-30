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

        // Whenever the home view model updates (username changes), update GoalsView's current user
        homeViewModel.addPropertyChangeListener(evt -> {
            try {
                String currentUsername2 = homeViewModel.getState().getUsername();
                if (currentUsername2 == null || currentUsername2.isEmpty()) {
                    // Clear user in goals view
                    if (goalsView instanceof view.GoalsView) {
                        ((view.GoalsView) goalsView).setCurrentUser(null);
                    }
                    return;
                }

                // Lookup stored user and metrics, similar to addGoalsUseCase() logic
                User entityUser2 = userDataAccessObject.get(currentUsername2);

                String name2 = "Guest";
                String password2 = "default";
                int age2 = 30;
                int height2 = 170;
                int weight2 = 0;

                if (entityUser2 != null) {
                    name2 = entityUser2.getName();
                    password2 = entityUser2.getPassword();

                    if (entityUser2.getAge() > 0) age2 = entityUser2.getAge();
                    if (entityUser2.getHeight() > 0) height2 = entityUser2.getHeight();
                    if (entityUser2.getWeight() > 0) weight2 = entityUser2.getWeight();
                }

                User currentUser2;
                if (age2 > 0 && height2 > 0 && weight2 > 0) {
                    currentUser2 = new User(name2, password2, age2, height2, weight2);
                } else {
                    currentUser2 = new User(name2, password2);
                }

                if (goalsView instanceof view.GoalsView) {
                    ((view.GoalsView) goalsView).setCurrentUser(currentUser2);
                }
            } catch (Exception ex) {
                System.err.println("AppBuilder: failed to update GoalsView current user: " + ex.getMessage());
            }
        });

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

        // Ensure GoalsView updates its current user after login is wired
        try {
            String cur = userDataAccessObject.getCurrentUsername();
            if (cur != null && goalsView instanceof view.GoalsView) {
                User eUser = userDataAccessObject.get(cur);
                if (eUser != null) {
                    ((view.GoalsView) goalsView).setCurrentUser(eUser);
                }
            }
        } catch (Exception ex) {
            System.err.println("AppBuilder: unable to set GoalsView current user after login wiring: " + ex.getMessage());
        }
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

        // Update GoalsView immediately when Settings are saved at runtime
        settingsViewModel.addPropertyChangeListener(evt -> {
            try {
                if ("settingsSaved".equals(evt.getPropertyName())) {
                    String curUser = userDataAccessObject.getCurrentUsername();
                    if (curUser == null || curUser.isEmpty()) return;

                    User stored = userDataAccessObject.get(curUser);
                    String n = "Guest"; String p = "default"; int a = 30; int h = 170; int w = 0;
                    if (stored != null) {
                        n = stored.getName(); p = stored.getPassword();
                        if (stored.getAge() > 0) a = stored.getAge();
                        if (stored.getHeight() > 0) h = stored.getHeight();
                        if (stored.getWeight() > 0) w = stored.getWeight();
                    }

                    // DO NOT use health metrics to override age/height/weight

                    User updated;
                    if (a > 0 && h > 0 && w > 0) updated = new User(n, p, a, h, w);
                    else updated = new User(n, p);

                    if (goalsView instanceof view.GoalsView) {
                        ((view.GoalsView) goalsView).setCurrentUser(updated);
                    }
                }
            } catch (Exception ex) {
                System.err.println("AppBuilder: failed to refresh GoalsView after settingsSaved: " + ex.getMessage());
            }
        });

        // Ensure GoalsView is updated if settings were changed while app running
        try {
            String cur2 = userDataAccessObject.getCurrentUsername();
            if (cur2 != null && goalsView instanceof view.GoalsView) {
                User eUser2 = userDataAccessObject.get(cur2);
                if (eUser2 != null) {
                    ((view.GoalsView) goalsView).setCurrentUser(eUser2);
                }
            }
        } catch (Exception ex) {
            System.err.println("AppBuilder: unable to set GoalsView current user after settings wiring: " + ex.getMessage());
        }
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
        // Use 0 to indicate weight not yet set (avoid showing a misleading 70kg default)
        int weight = 0;

        // If we found a stored user, use their name/password
        if (entityUser != null) {
            name = entityUser.getName();
            password = entityUser.getPassword();

            // Prefer age/height/weight stored on the User entity (Settings saves to users.csv)
            if (entityUser.getAge() > 0) {
                age = entityUser.getAge();
            }
            if (entityUser.getHeight() > 0) {
                height = entityUser.getHeight();
            }
            if (entityUser.getWeight() > 0) {
                weight = entityUser.getWeight();
            }
        }

        System.out.println("AppBuilder.addGoalsUseCase: currentUsername='" + currentUsername + "' -> name='" + name + "' age=" + age + " height=" + height + " weight=" + weight);

         // Build the User object that the goals use case will use
         User currentUser;
         // Only use the full constructor if the metrics are valid (positive numbers)
         if (age > 0 && height > 0 && weight > 0) {
             currentUser = new User(name, password, age, height, weight);
         } else {
             // Fallback to the simple user constructor (no metrics set yet)
             currentUser = new User(name, password);
         }

        // Wire up Goals use case
        GoalsPresenter goalsPresenter = new GoalsPresenter(goalsViewModel);
        // Pass the User DAO to the interactor so it can fetch current user and apply business rules
        GoalsInteractor goalsInteractor = new GoalsInteractor(goalsPresenter, userDataAccessObject);
        GoalsController goalsController = new GoalsController(goalsInteractor);

        goalsView.setGoalsController(goalsController);
        // Provide the current user entity to the view so it can display weight and redirect if needed
        if (goalsView instanceof view.GoalsView) {
            ((view.GoalsView) goalsView).setCurrentUser(currentUser);
            // Provide DAO to the view as optional fallback for display (interactor handles business rules now)
            ((view.GoalsView) goalsView).setUserDataAccess(userDataAccessObject);
        }

        return this;
    }

}
