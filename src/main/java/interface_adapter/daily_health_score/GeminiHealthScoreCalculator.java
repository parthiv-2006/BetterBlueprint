package interface_adapter.daily_health_score;

import services.GeminiAPIService;
import use_case.daily_health_score.HealthScoreCalculator;
import Entities.HealthMetrics;

/**
 * Adapter implementation of HealthScoreCalculator that delegates to GeminiAPIService.
 * This class acts as a bridge between the use case layer and the services layer,
 * following Clean Architecture principles.
 */
public class GeminiHealthScoreCalculator implements HealthScoreCalculator {

    private final GeminiAPIService geminiService;

    public GeminiHealthScoreCalculator(GeminiAPIService geminiService) {
        this.geminiService = geminiService;
    }

    @Override
    public int calculateScore(HealthMetrics metrics) throws Exception {
        try {
            return geminiService.calculateHealthScore(
                    metrics.getSleepHours(),
                    metrics.getExerciseMinutes(),
                    metrics.getCalories(),
                    metrics.getWaterIntake(),
                    metrics.getSteps()
            );
        } catch (Exception e) {
            System.err.println("Gemini API score calculation failed: " + e.getMessage());
            System.err.println("Using fallback algorithm.");
            return calculateFallbackScore(metrics);
        }
    }

    @Override
    public String generateFeedback(HealthMetrics metrics, int score) throws Exception {
        return geminiService.generateHealthFeedback(
                metrics.getSleepHours(),
                metrics.getExerciseMinutes(),
                metrics.getCalories(),
                metrics.getWaterIntake(),
                metrics.getSteps(),
                score
        );
    }

    /**
     * Fallback calculation if Gemini fails to return a valid score.
     * Uses a simple algorithm based on how close metrics are to recommended values.
     */
    private int calculateFallbackScore(HealthMetrics m) {
        double sleepHours = m.getSleepHours();
        double exerciseMinutes = m.getExerciseMinutes();
        int calories = m.getCalories();
        double waterIntake = m.getWaterIntake();
        int steps = m.getSteps();

        int score = 0;
        // Sleep score (0-25 points)
        if (sleepHours >= 7 && sleepHours <= 9) {
            score += 25;
        } else if (sleepHours >= 6 && sleepHours <= 10) {
            score += 17;
        } else if (sleepHours >= 5 && sleepHours <= 11) {
            score += 10;
        }

        // Exercise score (0-25 points)
        if (exerciseMinutes >= 30) {
            score += 25;
        } else if (exerciseMinutes >= 20) {
            score += 17;
        } else if (exerciseMinutes >= 10) {
            score += 10;
        }

        // Calories score (0-15 points)
        if (calories >= 1800 && calories <= 2500) {
            score += 15;
        } else if (calories >= 1500 && calories <= 3000) {
            score += 10;
        } else if (calories >= 1000 && calories <= 3500) {
            score += 5;
        }

        // Water score (0-15 points)
        if (waterIntake >= 2 && waterIntake <= 3) {
            score += 15;
        } else if (waterIntake >= 1.5 && waterIntake <= 4) {
            score += 10;
        } else if (waterIntake >= 1 && waterIntake <= 5) {
            score += 5;
        }

        // Steps score (0-20 points)
        if (steps >= 8000 && steps <= 12000) {
            score += 20;
        } else if (steps >= 6000 && steps <= 14000) {
            score += 13;
        } else if (steps >= 4000 && steps <= 16000) {
            score += 7;
        }

        return score;
    }
}
