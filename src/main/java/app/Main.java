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
                .addHealthInsightsUseCase()
                .addHomeView()
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