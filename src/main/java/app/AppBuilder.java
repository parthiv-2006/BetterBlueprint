package app;

import data_access.DailyHealthScoreDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.HealthMetricsDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.daily_health_score.DailyHealthScoreController;
import interface_adapter.daily_health_score.DailyHealthScorePresenter;
import interface_adapter.daily_health_score.DailyHealthScoreViewModel;
import interface_adapter.daily_health_score.GeminiHealthScoreCalculator;
import interface_adapter.home.HomeViewModel;
import interface_adapter.input_metrics.InputMetricsController;
import interface_adapter.input_metrics.InputMetricsPresenter;
import interface_adapter.input_metrics.InputMetricsViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
//import interface_adapter.logout.LogoutController;
//import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.daily_health_score.*;
import use_case.input_metrics.InputMetricsInputBoundary;
import use_case.input_metrics.InputMetricsInteractor;
import use_case.input_metrics.InputMetricsOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
//import use_case.logout.LogoutInputBoundary;
//import use_case.logout.LogoutInteractor;
//import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);
    final HealthMetricsDataAccessObject healthMetricsDataAccessObject = new HealthMetricsDataAccessObject(userDataAccessObject);

    // DAO version using a shared external database

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private HomeViewModel homeViewModel;
    private HomeView homeView;
    private LoginView loginView;
    private InputMetricsView inputMetricsView;
    private InputMetricsViewModel inputMetricsViewModel;
    private DailyHealthScoreViewModel dailyHealthScoreViewModel;
    private MyScoreView myScoreView;
    private DailyHealthScoreController dailyHealthScoreController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addHomeView() {

        inputMetricsViewModel = new InputMetricsViewModel();
        inputMetricsView = new InputMetricsView(inputMetricsViewModel);

        // DO NOT RECREATE ViewModel or MyScoreView here!
        homeViewModel = new HomeViewModel();
        homeView = new HomeView(homeViewModel, inputMetricsView, myScoreView);

        cardPanel.add(homeView, homeView.getViewName());
        return this;
    }


//    public AppBuilder addHomeView() {
//        // Create InputMetricsView first
//        inputMetricsViewModel = new InputMetricsViewModel();
//        inputMetricsView = new InputMetricsView(inputMetricsViewModel);
//
//        // Create DailyHealthScoreView
//        dailyHealthScoreViewModel = new DailyHealthScoreViewModel();
//        myScoreView = new MyScoreView(dailyHealthScoreViewModel, dailyHealthScoreController); // controller set later
//
//        // Create HomeView and pass InputMetricsView
//        homeViewModel = new HomeViewModel();
//        homeView = new HomeView(homeViewModel, inputMetricsView, myScoreView);
//        cardPanel.add(homeView, homeView.getViewName());
//        return this;
//    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                homeViewModel, loginViewModel, signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
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

//    public AppBuilder addDailyHealthScoreView() {
//        dailyHealthScoreViewModel = new DailyHealthScoreViewModel();
//        myScoreView = new MyScoreView(dailyHealthScoreViewModel, dailyHealthScoreController); // controller set later
//        cardPanel.add(myScoreView, "MyScoreView");
//        return this;
//    }

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

//    public AppBuilder addDailyHealthScoreUseCase() {
//
//        String apiKey = System.getenv("GEMINI_API_KEY");
//        HealthScoreCalculator scoreCalculator = new GeminiHealthScoreCalculator(apiKey);
//
//        DailyHealthScoreUserDataAccessInterface metricsDAO =
//                new DailyHealthScoreDataAccessObject();
//
//        // CONNECT PRESENTER
//        DailyHealthScoreOutputBoundary presenter =
//                new DailyHealthScorePresenter(dailyHealthScoreViewModel);
//
//        // CONNECT INTERACTOR
//        DailyHealthScoreInputBoundary interactor =
//                new DailyHealthScoreInteractor(metricsDAO, presenter, scoreCalculator);
//
//        // CREATE CONTROLLER
//        dailyHealthScoreController = new DailyHealthScoreController(interactor);
//
//        // NOW inject into the MyScoreView
//        if (myScoreView != null) {
//            myScoreView.setController(dailyHealthScoreController);
//        }
//
//        return this;
//    }
//

//    public AppBuilder addDailyHealthScoreUseCase() {
        // DAO for health metrics already exists
        // Use GeminiHealthScoreCalculator as HealthScoreCalculator implementation
//        String apiKey = System.getenv("GEMINI_API_KEY");
//        HealthScoreCalculator scoreCalculator = new GeminiHealthScoreCalculator(apiKey);
//        DailyHealthScoreUserDataAccessInterface dailyHealthScoreDataAccessObject = new DailyHealthScoreDataAccessObject();
//
//        // Presenter
//        DailyHealthScorePresenter presenter = new DailyHealthScorePresenter(dailyHealthScoreViewModel);
//
//        // Interactor
//        DailyHealthScoreInteractor interactor = new DailyHealthScoreInteractor(
//               dailyHealthScoreDataAccessObject,
//               presenter,
//               scoreCalculator
//            );
//
//            // Controller
//            dailyHealthScoreController = new DailyHealthScoreController(interactor);
//
//            // Wire controller to view
//            myScoreView = new MyScoreView(dailyHealthScoreViewModel, dailyHealthScoreController);
//
//            // Add the view to the card panel again to ensure it has controller
//            cardPanel.add(myScoreView, "MyScoreView");
//
//            return this;
        // Step 1: Create the view without controller
//        myScoreView = new MyScoreView(dailyHealthScoreViewModel, null);
//
// Step 2: Create presenter & interactor
//        DailyHealthScorePresenter presenter = new DailyHealthScorePresenter(dailyHealthScoreViewModel);
//        DailyHealthScoreInteractor interactor = new DailyHealthScoreInteractor(
//                dailyHealthScoreDataAccessObject,
//                presenter,
//                new GeminiHealthScoreCalculator(apiKey)
//        );
//
// Step 3: Create controller
//        dailyHealthScoreController = new DailyHealthScoreController(interactor);
//
// Step 4: Set controller in view
//        myScoreView.setController(dailyHealthScoreController);
//
// Step 5: Add to card panel
//        cardPanel.add(myScoreView, "MyScoreView");
//
//        return this;
//
//    }



        /**
        * Adds the Logout Use Case to the application.
        * @return this builder
        */
//    public AppBuilder addLogoutUseCase() {
//        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
//                homeViewModel, loginViewModel);
//
//        final LogoutInputBoundary logoutInteractor =
//                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);
//
//        return this;
//    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
