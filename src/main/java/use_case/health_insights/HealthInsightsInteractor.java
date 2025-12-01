package use_case.health_insights;

import Entities.HealthMetrics;
import Entities.User;
import data_access.HealthDataAccessInterface;
import data_access.UserDataAccessInterface;
import services.GeminiAPIService;

import java.util.List;

public class HealthInsightsInteractor implements HealthInsightsInputBoundary {
    private final HealthInsightsOutputBoundary healthInsightsOutputBoundary;
    private final HealthDataAccessInterface healthDataAccess;
    private final UserDataAccessInterface userDataAccess;
    private final GeminiAPIService geminiAPIService;

    public HealthInsightsInteractor(HealthInsightsOutputBoundary healthInsightsOutputBoundary,
                                    HealthDataAccessInterface healthDataAccess,
                                    UserDataAccessInterface userDataAccess,
                                    GeminiAPIService geminiAPIService) {
        this.healthInsightsOutputBoundary = healthInsightsOutputBoundary;
        this.healthDataAccess = healthDataAccess;
        this.userDataAccess = userDataAccess;
        this.geminiAPIService = geminiAPIService;
    }

    @Override
    public void execute(HealthInsightsInputData inputData) {
        String userId = inputData.getUserId();

        try {
            User user = userDataAccess.get(userId);
            if (user == null) {
                healthInsightsOutputBoundary.prepareFailView("User not found");
                return;
            }

            List<HealthMetrics> healthHistory = healthDataAccess.getHealthMetricsByUser(userId);
            if (healthHistory.isEmpty()) {
                healthInsightsOutputBoundary.prepareFailView("No health data available. Please log some metrics first.");
                return;
            }

            String analysisData = prepareAnalysisData(user, healthHistory);

            geminiAPIService.getHealthInsightsAsync(analysisData, new GeminiAPIService.InsightsCallback() {
                @Override
                public void onSuccess(String insights) {
                    HealthInsightsOutputData outputData = new HealthInsightsOutputData(insights);
                    healthInsightsOutputBoundary.prepareSuccessView(outputData);
                }

                @Override
                public void onError(String errorMessage) {
                    healthInsightsOutputBoundary.prepareFailView("Error generating insights: " + errorMessage);
                }
            });

        } catch (Exception e) {
            healthInsightsOutputBoundary.prepareFailView("Error generating insights: " + e.getMessage());
        }
    }

    private String prepareAnalysisData(User user, List<HealthMetrics> healthHistory) {
        StringBuilder data = new StringBuilder();

        data.append("User Profile: ").append(user.getAge()).append(" years old, ")
                .append(user.getHeight()).append("cm tall, ").append(user.getWeight()).append("kg. ");

        HealthMetrics recent = healthHistory.get(healthHistory.size() - 1);
        data.append("Most Recent Daily Metrics: ")
                .append("Sleep: ").append(recent.getSleepHours()).append(" hours, ")
                .append("Steps: ").append(recent.getSteps()).append(", ")
                .append("Water Intake: ").append(recent.getWaterIntake()).append(" liters, ")
                .append("Exercise: ").append(recent.getExerciseMinutes()).append(" minutes, ")
                .append("Calories: ").append(recent.getCalories()).append(". ");

        if (healthHistory.size() >= 3) {
            data.append("Historical Trends: ").append(analyzeTrends(healthHistory));
        }

        return data.toString();
    }

    private String analyzeTrends(List<HealthMetrics> history) {
        double avgSleep = history.stream().mapToDouble(HealthMetrics::getSleepHours).average().orElse(0);
        double avgSteps = history.stream().mapToDouble(HealthMetrics::getSteps).average().orElse(0);
        double avgWater = history.stream().mapToDouble(HealthMetrics::getWaterIntake).average().orElse(0);
        double avgExercise = history.stream().mapToDouble(HealthMetrics::getExerciseMinutes).average().orElse(0);
        double avgCalories = history.stream().mapToDouble(HealthMetrics::getCalories).average().orElse(0);

        return String.format("Average Sleep: %.1f hours, Average Steps: %.0f, Average Water: %.1fL, Average Exercise: %.1f minutes, Average Calories: %.0f.",
                avgSleep, avgSteps, avgWater, avgExercise, avgCalories);
    }
}