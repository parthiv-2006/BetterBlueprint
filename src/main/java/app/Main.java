package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addDailyHealthScoreUseCase()
                .addHealthInsightsUseCase()
                .addSettingsViewModel()
                .addHomeView()
                .addSettingsView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addSettingsUseCase()
                .addLogoutUseCase()
                .addChangePasswordUseCase()
                .addInputMetricsUseCase()
                .addGoalsUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}