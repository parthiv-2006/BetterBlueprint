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
        System.out.println("HealthInsightsInteractor: Generating insights for user: " + userId);

        try {
            // Get user data
            User user = userDataAccess.get(userId);
            if (user == null) {
                System.err.println("HealthInsightsInteractor: User not found - " + userId);
                healthInsightsOutputBoundary.prepareFailView("User not found");
                return;
            }

            // Get health metrics history
            List<HealthMetrics> healthHistory = healthDataAccess.getHealthMetricsByUser(userId);
            System.out.println("HealthInsightsInteractor: Found " + healthHistory.size() + " health records");

            if (healthHistory.isEmpty()) {
                System.err.println("HealthInsightsInteractor: No health data available for user: " + userId);
                healthInsightsOutputBoundary.prepareFailView("No health data available. Please log some metrics first.");
                return;
            }

            // Generate insights
            String insights = generateAIInsights(user, healthHistory);
            HealthInsightsOutputData outputData = new HealthInsightsOutputData(insights);

            System.out.println("HealthInsightsInteractor: Successfully generated insights");
            healthInsightsOutputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            System.err.println("HealthInsightsInteractor: Error generating insights - " + e.getMessage());
            e.printStackTrace();
            healthInsightsOutputBoundary.prepareFailView("Error generating insights: " + e.getMessage());
        }
    }

    private String generateAIInsights(User user, List<HealthMetrics> healthHistory) {
        String analysisData = prepareAnalysisData(user, healthHistory);
        System.out.println("HealthInsightsInteractor: Sending to Gemini API - " + analysisData);
        return geminiAPIService.getHealthInsights(analysisData);
    }

    private String prepareAnalysisData(User user, List<HealthMetrics> healthHistory) {
        StringBuilder data = new StringBuilder();

        // User profile
        data.append("User Profile: ").append(user.getAge()).append(" years old, ")
                .append(user.getHeight()).append("cm tall, ").append(user.getWeight()).append("kg. ");

        // Recent metrics (most recent entry)
        HealthMetrics recent = healthHistory.get(healthHistory.size() - 1);
        data.append("Most Recent Daily Metrics: ")
                .append("Sleep: ").append(recent.getSleepHour()).append(" hours, ")
                .append("Steps: ").append(recent.getSteps()).append(", ")
                .append("Water Intake: ").append(recent.getWaterLitres()).append(" liters, ")
                .append("Exercise: ").append(recent.getExerciseMinutes()).append(" minutes, ")
                .append("Calories: ").append(recent.getCalories()).append(". ");

        // Historical trends if we have multiple entries
        if (healthHistory.size() >= 3) {
            data.append("Historical Trends: ").append(analyzeTrends(healthHistory));
        } else if (healthHistory.size() > 1) {
            data.append("Multiple data points available for analysis. ");
        }

        System.out.println("HealthInsightsInteractor: Prepared analysis data - " + data.toString());
        return data.toString();
    }

    private String analyzeTrends(List<HealthMetrics> history) {
        StringBuilder trends = new StringBuilder();

        // Calculate averages
        double avgSleep = history.stream().mapToDouble(HealthMetrics::getSleepHour).average().orElse(0);
        double avgSteps = history.stream().mapToDouble(HealthMetrics::getSteps).average().orElse(0);
        double avgWater = history.stream().mapToDouble(HealthMetrics::getWaterLitres).average().orElse(0);
        double avgExercise = history.stream().mapToDouble(HealthMetrics::getExerciseMinutes).average().orElse(0);
        double avgCalories = history.stream().mapToDouble(HealthMetrics::getCalories).average().orElse(0);

        trends.append(String.format("Average Sleep: %.1f hours, ", avgSleep));
        trends.append(String.format("Average Steps: %.0f, ", avgSteps));
        trends.append(String.format("Average Water: %.1fL, ", avgWater));
        trends.append(String.format("Average Exercise: %.1f minutes, ", avgExercise));
        trends.append(String.format("Average Calories: %.0f. ", avgCalories));

        // Add some basic trend analysis
        HealthMetrics oldest = history.get(0);
        HealthMetrics newest = history.get(history.size() - 1);

        if (newest.getSleepHour() > oldest.getSleepHour()) {
            trends.append("Sleep duration is improving. ");
        } else if (newest.getSleepHour() < oldest.getSleepHour()) {
            trends.append("Sleep duration needs attention. ");
        }

        if (newest.getSteps() > oldest.getSteps()) {
            trends.append("Physical activity is increasing. ");
        }

        if (newest.getWaterLitres() > oldest.getWaterLitres()) {
            trends.append("Hydration is improving. ");
        }

        return trends.toString();
    }
}