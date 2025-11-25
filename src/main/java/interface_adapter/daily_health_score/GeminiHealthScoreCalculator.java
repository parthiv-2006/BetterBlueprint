package interface_adapter.daily_health_score;

import use_case.daily_health_score.HealthScoreCalculator;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Implementation of HealthScoreCalculator using Google Gemini API.
 */

public class GeminiHealthScoreCalculator implements HealthScoreCalculator {

    private final String apiKey;
    private final HttpClient client;
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

    public GeminiHealthScoreCalculator(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public int calculateScore(double sleepHours, double exerciseMinutes,
                              int calories, double waterIntake) {

        String prompt =
                "You are a health score calculator. Analyze these daily health metrics and calculate a health score from 0-100.\n\n" +
                "Metrics:\n" +
                "- Sleep: " + sleepHours + " hours (Recommended: 7-9 hours)\n" +
                "- Exercise: " + exerciseMinutes + " minutes (Recommended: 30+ minutes)\n" +
                "- Calories: " + calories + " kcal (Recommended: 2000-2500 kcal)\n" +
                "- Water: " + waterIntake + " liters (Recommended: 2-3 liters)\n\n" +
                "Scoring guidelines:\n" +
                "- 90-100: Excellent - All metrics near optimal\n" +
                "- 75-89: Good - Most metrics healthy\n" +
                "- 60-74: Fair - Some improvement needed\n" +
                "- 40-59: Poor - Several metrics need attention\n" +
                "- 0-39: Very Poor - Most metrics unhealthy\n\n" +
                "Calculate the score by comparing each metric to its recommended range. " +
                "Weight sleep and exercise slightly higher than calories and water.\n\n" +
                "Respond with ONLY the integer score (0-100), nothing else.";

        String result = callGemini(prompt).trim();

        try {
            // First try to parse directly
            int score = Integer.parseInt(result);

            // Validate range
            if (score < 0 || score > 100) {
                return calculateFallbackScore(sleepHours, exerciseMinutes, calories, waterIntake);
            }

            return score;
        } catch (NumberFormatException e) {
            // Try to extract first number
            String numbersOnly = result.replaceAll("[^0-9]", "");
            if (!numbersOnly.isEmpty() && numbersOnly.length() <= 3) {
                try {
                    int score = Integer.parseInt(numbersOnly);
                    if (score >= 0 && score <= 100) {
                        return score;
                    }
                } catch (NumberFormatException ex) {
                    // Fall through to fallback
                }
            }

            return calculateFallbackScore(sleepHours, exerciseMinutes, calories, waterIntake);
        }
    }

    /**
     * Fallback calculation if Gemini fails to return a valid score.
     * Uses a simple algorithm based on how close metrics are to recommended values.
     */
    private int calculateFallbackScore(double sleepHours, double exerciseMinutes,
                                       int calories, double waterIntake) {
        int score = 0;

        // Sleep score (0-30 points)
        if (sleepHours >= 7 && sleepHours <= 9) {
            score += 30;
        } else if (sleepHours >= 6 && sleepHours <= 10) {
            score += 20;
        } else if (sleepHours >= 5 && sleepHours <= 11) {
            score += 10;
        }

        // Exercise score (0-30 points)
        if (exerciseMinutes >= 30) {
            score += 30;
        } else if (exerciseMinutes >= 20) {
            score += 20;
        } else if (exerciseMinutes >= 10) {
            score += 10;
        }

        // Calories score (0-20 points)
        if (calories >= 1800 && calories <= 2500) {
            score += 20;
        } else if (calories >= 1500 && calories <= 3000) {
            score += 15;
        } else if (calories >= 1000 && calories <= 3500) {
            score += 10;
        }

        // Water score (0-20 points)
        if (waterIntake >= 2 && waterIntake <= 3) {
            score += 20;
        } else if (waterIntake >= 1.5 && waterIntake <= 4) {
            score += 15;
        } else if (waterIntake >= 1 && waterIntake <= 5) {
            score += 10;
        }

        return score;
    }

    @Override
    public String generateFeedback(double sleepHours, double exerciseMinutes,
                                   int calories, double waterIntake, int score) {

        String prompt =
                "A user has these daily health metrics:\n" +
                "- Sleep: " + sleepHours + " hours (Recommended: 7-9)\n" +
                "- Exercise: " + exerciseMinutes + " minutes (Recommended: 30+)\n" +
                "- Calories: " + calories + " kcal (Recommended: 2000-2500)\n" +
                "- Water: " + waterIntake + " liters (Recommended: 2-3)\n" +
                "- Overall Score: " + score + "/100\n\n" +
                "Provide feedback and reasoning for their overall score in 2-3 sentences (max 50 words). " +
                "Mention what they're doing well and one specific improvement they should focus on. " +
                "Be positive and actionable.";

        String feedback = callGemini(prompt).trim();

        return feedback;
    }

    private String callGemini(String prompt) {
        try {
            // Use generation config for more controlled responses
            String jsonBody = """
            {
              "contents": [{
                "parts": [{
                    "text": "%s"
                }]
              }],
              "generationConfig": {
                "temperature": 0.3,
                "topK": 1,
                "topP": 1,
                "maxOutputTokens": 100
              }
            }
            """.formatted(prompt.replace("\"", "\\\""));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_URL + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() != 200) {
                return "Error: " + response.body();
            }

            return extractTextFromGeminiResponse(response.body());
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Unable to retrieve response from Gemini: " + e.getMessage();
        }
    }

    /**
     * Extracts the model's text output from Gemini's JSON response.
     */
    private String extractTextFromGeminiResponse(String json) {
        // naive extraction: look for "text": " ... "
        int start = json.indexOf("\"text\":");
        if (start == -1) return json;

        start = json.indexOf("\"", start + 7) + 1;
        int end = json.indexOf("\"", start);

        if (end == -1) return json;
        return json.substring(start, end);
    }
}
