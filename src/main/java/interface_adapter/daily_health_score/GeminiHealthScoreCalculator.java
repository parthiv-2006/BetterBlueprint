package interface_adapter.daily_health_score;

import use_case.daily_health_score.HealthScoreCalculator;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiHealthScoreCalculator implements HealthScoreCalculator {

    private final String apiKey;
    private final HttpClient client;
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent?key=";

    public GeminiHealthScoreCalculator(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public int calculateScore(double sleepHours, double exerciseMinutes,
                              int calories, double waterIntake) {

        String prompt =
                "Given the following daily health metrics:\n" +
                        "Sleep hours: " + sleepHours + "\n" +
                        "Exercise minutes: " + exerciseMinutes + "\n" +
                        "Calories consumed: " + calories + "\n" +
                        "Water intake (litres): " + waterIntake + "\n\n" +
                        "Return ONLY an integer from 0 to 100 representing a daily health score.";

        String result = callGemini(prompt).trim();

        try {
            return Integer.parseInt(result.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 50; // fallback default
        }
    }

    @Override
    public String generateFeedback(double sleepHours, double exerciseMinutes,
                                   int calories, double waterIntake, int score) {

        String prompt =
                "A user has the following metrics for today:\n" +
                        "Sleep hours: " + sleepHours + "\n" +
                        "Exercise minutes: " + exerciseMinutes + "\n" +
                        "Calories consumed: " + calories + "\n" +
                        "Water intake (litres): " + waterIntake + "\n" +
                        "Daily health score: " + score + "\n\n" +
                        "Provide a brief, encouraging explanation of the score and suggestions for improvement. Max 50 words";

        return callGemini(prompt).trim();
    }

    private String callGemini(String prompt) {
        try {
            String jsonBody = """
            {
              "contents": [{
                "parts": [{
                    "text": "%s"
                }]
              }]
            }
            """.formatted(prompt.replace("\"", "\\\""));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_URL + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log response for debugging
            System.out.println("Gemini API Response Status: " + response.statusCode());
            System.out.println("Gemini API Response Body: " + response.body());

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
     * This is a minimal JSON parsing strategy—replace with a real JSON
     * library (Jackson/Gson) if needed.
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
