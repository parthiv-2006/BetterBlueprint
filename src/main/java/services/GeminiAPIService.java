package services;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class GeminiAPIService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String MEDIA_TYPE_JSON = "application/json";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse(MEDIA_TYPE_JSON);

    private static final String QUERY_PARAM_KEY = "?key=";

    private final String apiKey;
    private final OkHttpClient client;

    public GeminiAPIService() {
        this.apiKey = System.getenv("GEMINI_API_KEY");

        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public void getHealthInsightsAsync(String healthData, InsightsCallback callback) {
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return getHealthInsights(healthData);
            }

            @Override
            protected void done() {
                try {
                    String insights = get();
                    callback.onSuccess(insights);
                } catch (Exception e) {
                    callback.onError(e.getMessage());
                }
            }
        }.execute();
    }

    private String getHealthInsights(String healthData) {
        if (apiKey == null || apiKey.isEmpty()) {
            return "API key not configured. Please set the GEMINI_API_KEY environment variable to use AI insights.";
        }
        try {
            String prompt = "You are a health coach. Analyze this health data and provide 2-3 specific, actionable insights. " +
                    "Focus on sleep hours, steps, water intake, exercise minutes, and calories. " +
                    "Be specific about the numbers mentioned and give practical recommendations. " +
                    "Keep it under 200 words. Data: " + healthData;

            JSONObject requestBody = createRequestBody(prompt);
            String requestBodyString = requestBody.toString();

            Request request = new Request.Builder()
                    .url(GEMINI_API_URL + QUERY_PARAM_KEY + apiKey)
                    .post(RequestBody.create(requestBodyString, JSON_MEDIA_TYPE))
                    .addHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_JSON)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body().string();
                    throw new RuntimeException("API request failed: " + response.code() + " - " + errorBody);
                }

                String responseBody = response.body().string();
                return parseApiResponse(responseBody);
            }
        } catch (Exception e) {
            throw new RuntimeException("API error: " + e.getMessage());
        }
    }

    private JSONObject createRequestBody(String prompt) {
        JSONObject requestBody = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject content = new JSONObject();
        JSONArray parts = new JSONArray();
        JSONObject part = new JSONObject();

        part.put("text", prompt);
        parts.put(part);
        content.put("parts", parts);
        contents.put(content);
        requestBody.put("contents", contents);

        JSONObject generationConfig = new JSONObject();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("topP", 0.95);
        generationConfig.put("maxOutputTokens", 512);
        requestBody.put("generationConfig", generationConfig);

        return requestBody;
    }

    private String parseApiResponse(String responseBody) {
        try {
            JSONObject json = new JSONObject(responseBody);
            JSONArray candidates = json.getJSONArray("candidates");
            JSONObject candidate = candidates.getJSONObject(0);
            JSONObject content = candidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            String text = parts.getJSONObject(0).getString("text");

            return text.trim();
        } catch (Exception e) {
            return "Based on your health metrics, I recommend focusing on consistent sleep patterns, regular physical activity, and balanced nutrition for optimal wellness.";
        }
    }

    public interface InsightsCallback {
        void onSuccess(String insights);
        void onError(String errorMessage);
    }

    /**
     * Calculate a health score (0-100) based on daily health metrics using Gemini API.
     *
     * @param sleepHours Sleep duration in hours
     * @param exerciseMinutes Exercise duration in minutes
     * @param calories Caloric intake
     * @param waterIntake Water intake in liters
     * @param steps Daily step count
     * @return Health score from 0-100
     * @throws Exception if the API call fails
     */
    public int calculateHealthScore(double sleepHours, double exerciseMinutes,
                                    int calories, double waterIntake, int steps) throws Exception {
        String prompt =
                "You are a health score calculator. Analyze these daily health metrics and calculate a health score from 0-100.\n\n" +
                        "Metrics:\n" +
                        "- Sleep: " + sleepHours + " hours (Recommended: 7-9 hours)\n" +
                        "- Exercise: " + exerciseMinutes + " minutes (Recommended: 30+ minutes)\n" +
                        "- Calories: " + calories + " kcal (Recommended: 2000-2500 kcal)\n" +
                        "- Water: " + waterIntake + " liters (Recommended: 2-3 liters)\n" +
                        "- Steps: " + steps + " steps (Recommended: 8000-10000 steps)\n\n" +
                        "Scoring guidelines:\n" +
                        "- 90-100: Excellent - All metrics near optimal\n" +
                        "- 75-89: Good - Most metrics healthy\n" +
                        "- 60-74: Fair - Some improvement needed\n" +
                        "- 40-59: Poor - Several metrics need attention\n" +
                        "- 0-39: Very Poor - Most metrics unhealthy\n\n" +
                        "Calculate the score by comparing each metric to its recommended range. " +
                        "Weight sleep, exercise, and steps slightly higher than calories and water.\n\n" +
                        "Respond with ONLY the integer score (0-100), nothing else.";

        JSONObject requestBody = createRequestBody(prompt);
        String requestBodyString = requestBody.toString();

        Request request = new Request.Builder()
                .url(GEMINI_API_URL + QUERY_PARAM_KEY + apiKey)
                .post(RequestBody.create(requestBodyString, JSON_MEDIA_TYPE))
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_JSON)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body().string();
                throw new RuntimeException("API request failed: " + response.code() + " - " + errorBody);
            }

            String responseBody = response.body().string();
            String result = parseApiResponse(responseBody).trim();

            // Parse the score
            try {
                int score = Integer.parseInt(result);
                if (score < 0 || score > 100) {
                    throw new IllegalArgumentException("Score out of valid range: " + score);
                }
                return score;
            } catch (NumberFormatException e) {
                // Try to extract first number
                String numbersOnly = result.replaceAll("[^0-9]", "");
                if (!numbersOnly.isEmpty() && numbersOnly.length() <= 3) {
                    int score = Integer.parseInt(numbersOnly);
                    if (score >= 0 && score <= 100) {
                        return score;
                    }
                }
                throw new IllegalArgumentException("Unable to parse score from response: " + result);
            }
        }
    }

    /**
     * Generate personalized health feedback based on metrics and score using Gemini API.
     *
     * @param sleepHours Sleep duration in hours
     * @param exerciseMinutes Exercise duration in minutes
     * @param calories Caloric intake
     * @param waterIntake Water intake in liters
     * @param steps Daily step count
     * @param score The calculated health score
     * @return Personalized health feedback
     * @throws Exception if the API call fails
     */
    public String generateHealthFeedback(double sleepHours, double exerciseMinutes,
                                        int calories, double waterIntake, int steps, int score) throws Exception {
        String prompt =
                "A user has these daily health metrics:\n" +
                        "- Sleep: " + sleepHours + " hours (Recommended: 7-9)\n" +
                        "- Exercise: " + exerciseMinutes + " minutes (Recommended: 30+)\n" +
                        "- Calories: " + calories + " kcal (Recommended: 2000-2500)\n" +
                        "- Water: " + waterIntake + " liters (Recommended: 2-3)\n" +
                        "- Steps: " + steps + " steps (Recommended: 8000-10000)\n" +
                        "- Overall Score: " + score + "/100\n\n" +
                        "Provide feedback and reasoning for their overall score in 2-3 sentences (max 50 words). " +
                        "Mention what they're doing well and one specific improvement they should focus on. " +
                        "Be positive and actionable.";

        JSONObject requestBody = createRequestBody(prompt);
        String requestBodyString = requestBody.toString();

        Request request = new Request.Builder()
                .url(GEMINI_API_URL + QUERY_PARAM_KEY + apiKey)
                .post(RequestBody.create(requestBodyString, JSON_MEDIA_TYPE))
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_JSON)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body().string();
                throw new RuntimeException("API request failed: " + response.code() + " - " + errorBody);
            }

            String responseBody = response.body().string();
            return parseApiResponse(responseBody).trim();
        }
    }
}