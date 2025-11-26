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
            // Get user data
            User user = userDataAccess.get(userId); // Changed to get() method
            if (user == null) {
                healthInsightsOutputBoundary.prepareFailView("User not found");
                return;
            }

            // Get health metrics history
            List<HealthMetrics> healthHistory = healthDataAccess.getHealthMetricsByUser(userId);
            if (healthHistory.isEmpty()) {
                healthInsightsOutputBoundary.prepareFailView("No health data available. Please log some metrics first.");
                return;
            }

            // Generate insights
            String insights = generateAIInsights(user, healthHistory);

            HealthInsightsOutputData outputData = new HealthInsightsOutputData(insights);
            healthInsightsOutputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            healthInsightsOutputBoundary.prepareFailView("Error generating insights: " + e.getMessage());
        }
    }

    private String generateAIInsights(User user, List<HealthMetrics> healthHistory) {
        String analysisData = prepareAnalysisData(user, healthHistory);
        return geminiAPIService.getHealthInsights(analysisData);
    }

    private String prepareAnalysisData(User user, List<HealthMetrics> healthHistory) {
        StringBuilder data = new StringBuilder();

        // Add user profile
        data.append("User Profile: Age ").append(user.getAge())
                .append(", Height ").append(user.getHeight())
                .append("cm, Weight ").append(user.getWeight()).append("kg. ");

        // Add recent metrics - CORRECT getter methods
        HealthMetrics recent = healthHistory.get(healthHistory.size() - 1);
        data.append("Recent: Sleep ").append(recent.getSleepHour()).append("h, Steps ")
                .append(recent.getSteps()).append(", Water ")
                .append(recent.getWaterLitres()).append("L, Exercise ").append(recent.getExerciseMinutes())
                .append("min, Calories ").append(recent.getCalories()).append(". ");

        // Add trends if available
        if (healthHistory.size() >= 3) {
            data.append("Trends: ").append(analyzeTrends(healthHistory));
        }

        return data.toString();
    }

    private String analyzeTrends(List<HealthMetrics> history) {
        double avgSleep = history.stream().mapToDouble(HealthMetrics::getSleepHour).average().orElse(0);
        double avgWater = history.stream().mapToDouble(HealthMetrics::getWaterLitres).average().orElse(0);
        return String.format("Avg sleep: %.1fh, Avg water: %.1fL", avgSleep, avgWater);
    }
}