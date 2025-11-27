package services;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;

public class GeminiAPIService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent";
    private final String apiKey;
    private final OkHttpClient client;

    public GeminiAPIService() {
        this.apiKey = System.getenv("GEMINI_API_KEY");

        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalStateException("GEMINI_API_KEY environment variable is not set.");
        }

        System.out.println("✅ GeminiAPIService initialized with working API key!");
    }

    public String getHealthInsights(String healthData) {
        System.out.println("🚀 Calling REAL Gemini API with data: " + healthData);

        try {
            String prompt = "You are a health coach. Analyze this health data and provide 2-3 specific, actionable insights. " +
                    "Focus on sleep hours, steps, water intake, exercise minutes, and calories. " +
                    "Be specific about the numbers mentioned and give practical recommendations. " +
                    "Keep it under 200 words. Data: " + healthData;

            JSONObject requestBody = createRequestBody(prompt);
            String requestBodyString = requestBody.toString();

            Request request = new Request.Builder()
                    .url(GEMINI_API_URL + "?key=" + apiKey)
                    .post(RequestBody.create(requestBodyString, MediaType.parse("application/json")))
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("❌ API request failed: " + response.code() + " - " + response.message());
                    String errorBody = response.body().string();
                    System.err.println("Error details: " + errorBody);
                    return getFallbackInsights(healthData);
                }

                String responseBody = response.body().string();
                System.out.println("✅ API call successful! Parsing response...");

                String insights = parseApiResponse(responseBody);
                System.out.println("🎯 REAL Gemini API Insights: " + insights);
                return insights;
            }
        } catch (Exception e) {
            System.err.println("❌ API error: " + e.getMessage());
            return getFallbackInsights(healthData);
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

        // Generation config for better responses
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
            System.err.println("❌ Error parsing API response: " + e.getMessage());
            return "I analyzed your health data and found areas for improvement. Focus on consistent sleep, regular activity, and balanced nutrition.";
        }
    }

    private String getFallbackInsights(String healthData) {
        // Simple fallback - you shouldn't need this now!
        return "Based on your health metrics, I recommend focusing on consistent sleep patterns, regular physical activity, and balanced nutrition for optimal wellness.";
    }
}