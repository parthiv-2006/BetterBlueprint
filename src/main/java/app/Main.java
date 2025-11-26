package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addDailyHealthScoreUseCase()
                .addSettingsView()
                .addHealthInsightsUseCase()  // MUST BE BEFORE HomeView
                .addHomeView()               // Now HomeView will get the properly initialized HealthInsightsView
                .addSignupUseCase()
                .addLoginUseCase()
                .addSettingsUseCase()
                .addLogoutUseCase()
                .addChangePasswordUseCase()
                .addInputMetricsUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}