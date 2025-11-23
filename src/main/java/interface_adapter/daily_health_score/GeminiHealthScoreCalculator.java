package interface_adapter.daily_health_score;

import Entities.HealthMetrics;
import use_case.daily_health_score.HealthScoreCalculator;

public class GeminiHealthScoreCalculator implements HealthScoreCalculator {
    private final String apiKey;

    public GeminiHealthScoreCalculator(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public int calculateScore(HealthMetrics metrics) throws Exception {

        String prompt = """
            You are a health scoring engine.
            Given the following metrics, output only an integer 0–100 representing the health score.

            Metrics:
            exercise_minutes: %.1f
            sleep_hours: %.1f
            calories: %d
            water_intake_liters: %.1f
        """.formatted(
                metrics.getExerciseMinutes(),
                metrics.getSleepHours(),
                metrics.getCalories(),
                metrics.getWaterIntake()
        );

        // Call Gemini API
        String modelResponse = postToGemini(prompt);

        return Integer.parseInt(modelResponse.trim());
    }

    @Override
    public String generateFeedback(HealthMetrics metrics, int score) throws Exception {

        String prompt = """
            Provide a short, friendly health summary for someone with score %d and the following metrics:
            sleep_hours: %.1f
            calories: %d
            water_intake_liters: %.1f
            exercise_minutes: %.1f
            Max 25 words.
        """.formatted(score,
            metrics.getSleepHours(),
            metrics.getCalories(),
            metrics.getWaterIntake(),
            metrics.getExerciseMinutes());

        return postToGemini(prompt).trim();
    }

    private String postToGemini(String prompt) throws Exception {
        // Your actual HTTP POST logic here
        // Return the text content of Gemini’s response
        return "";  // placeholder
    }
}

