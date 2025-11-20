package services;

import okhttp3.*;
import org.json.JSONObject;

public class GeminiAPIService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent";
    private final String apiKey;
    private final OkHttpClient client;

    public GeminiAPIService(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
    }

    public String getHealthInsights(String healthData) {
        try {
            String prompt = "Provide 2-3 concise, actionable health insights based on this data: " + healthData;

            JSONObject requestBody = createRequestBody(prompt);

            Request request = new Request.Builder()
                    .url(GEMINI_API_URL + "?key=" + apiKey)
                    .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return parseApiResponse(response.body().string());
                }
            }
        } catch (Exception e) {
            // Fallback insights if API fails
        }
        return "Based on your data: Focus on consistent sleep patterns and adequate hydration for better wellness.";
    }

    private JSONObject createRequestBody(String prompt) {
        JSONObject requestBody = new JSONObject();
        JSONObject content = new JSONObject();
        JSONObject part = new JSONObject();

        part.put("text", prompt);
        content.put("parts", new JSONObject[] {part});
        requestBody.put("contents", new JSONObject[] {content});

        return requestBody;
    }

    private String parseApiResponse(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        } catch (Exception e) {
            return "Personalized health insights based on your recent activity.";
        }
    }
}
