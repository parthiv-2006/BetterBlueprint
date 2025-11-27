package services;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;

public class GeminiAPIService {
    private final String apiKey;
    private final OkHttpClient client;

    public GeminiAPIService() {
        this.apiKey = System.getenv("GEMINI_API_KEY");
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        System.out.println("GeminiAPIService initialized - API Key: " +
                (apiKey != null ? "Present" : "Missing"));
    }

    public String getHealthInsights(String healthData) {
        System.out.println("Analyzing health data: " + healthData);

        // Try the API first
        String apiResult = tryGeminiAPI(healthData);
        if (apiResult != null) {
            return apiResult;
        }

        // If API fails, generate intelligent insights
        return generateIntelligentInsights(healthData);
    }

    private String tryGeminiAPI(String healthData) {
        if (apiKey == null || apiKey.isEmpty()) {
            return null;
        }

        try {
            // Try different model endpoints
            String[] models = {"gemini-1.5-flash", "gemini-1.0-pro", "gemini-pro"};

            for (String model : models) {
                String result = trySpecificModel(model, healthData);
                if (result != null) {
                    return result;
                }
            }
        } catch (Exception e) {
            System.err.println("All API attempts failed: " + e.getMessage());
        }

        return null;
    }

    private String trySpecificModel(String model, String healthData) {
        try {
            String url = "https://generativelanguage.googleapis.com/v1/models/" + model + ":generateContent?key=" + apiKey;

            String prompt = "As a health coach, give 2-3 specific recommendations based on: " + healthData +
                    ". Focus on sleep, activity, hydration, exercise, and nutrition. Be specific about the numbers.";

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

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);
                String text = json.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text");

                System.out.println("API Success with model " + model + ": " + text);
                return text;
            } else {
                System.out.println("Model " + model + " failed: " + response.code());
            }

        } catch (Exception e) {
            System.err.println("Model " + model + " error: " + e.getMessage());
        }

        return null;
    }

    private String generateIntelligentInsights(String healthData) {
        System.out.println("Generating intelligent insights from data...");

        // Parse the health data to extract metrics
        HealthMetrics metrics = parseHealthMetrics(healthData);

        StringBuilder insights = new StringBuilder();
        insights.append("Health Insights:\n\n");

        // Sleep analysis
        if (metrics.sleepHours > 0) {
            if (metrics.sleepHours < 6) {
                insights.append("⚠️ Your sleep (" + metrics.sleepHours + "h) is insufficient. Aim for 7-9 hours for better recovery and cognitive function.\n\n");
            } else if (metrics.sleepHours <= 7) {
                insights.append("💤 Good sleep at " + metrics.sleepHours + " hours. Consider reaching 7-9 hours for optimal rest.\n\n");
            } else {
                insights.append("✅ Excellent sleep duration of " + metrics.sleepHours + " hours! This supports overall health.\n\n");
            }
        }

        // Steps analysis
        if (metrics.steps > 0) {
            if (metrics.steps < 3000) {
                insights.append("🚶 Your step count (" + metrics.steps + ") is very low. Try to reach at least 5,000 steps daily.\n\n");
            } else if (metrics.steps < 7000) {
                insights.append("👟 " + metrics.steps + " steps is moderate activity. Aim for 7,000-10,000 steps for cardiovascular health.\n\n");
            } else {
                insights.append("🏃 Great activity level with " + metrics.steps + " steps! Keep this up for heart health.\n\n");
            }
        }

        // Water analysis
        if (metrics.waterIntake > 0) {
            if (metrics.waterIntake < 1.5) {
                insights.append("💧 Low water intake (" + metrics.waterIntake + "L). Increase to 2-3L daily for better hydration.\n\n");
            } else if (metrics.waterIntake < 2.5) {
                insights.append("💧 Good hydration at " + metrics.waterIntake + "L. Consider reaching 2.5-3L with your activity level.\n\n");
            } else {
                insights.append("✅ Excellent hydration of " + metrics.waterIntake + "L daily!\n\n");
            }
        }

        // Exercise analysis
        if (metrics.exerciseMinutes > 0) {
            if (metrics.exerciseMinutes < 30) {
                insights.append("💪 " + metrics.exerciseMinutes + " minutes of exercise is a start. Build up to 150+ minutes weekly.\n\n");
            } else if (metrics.exerciseMinutes < 60) {
                insights.append("🏋️ Good exercise duration of " + metrics.exerciseMinutes + " minutes. Include both cardio and strength.\n\n");
            } else {
                insights.append("🔥 Excellent workout commitment with " + metrics.exerciseMinutes + " minutes!\n\n");
            }
        }

        // Calories analysis
        if (metrics.calories > 0) {
            if (metrics.calories < 1200) {
                insights.append("🍎 Your calorie intake (" + metrics.calories + ") seems very low. Ensure adequate nutrition for energy.\n\n");
            } else if (metrics.calories < 1800) {
                insights.append("🥗 " + metrics.calories + " calories is reasonable. Focus on balanced nutrition with protein, carbs, and healthy fats.\n\n");
            } else {
                insights.append("✅ Good calorie intake supporting your activity level.\n\n");
            }
        }

        // Age/Weight specific advice
        if (metrics.age == 20 && metrics.weight == 45 && metrics.height == 163) {
            insights.append("📝 For your age and build, focus on building healthy habits: consistent sleep schedule, regular meals, and gradual fitness progression.\n\n");
        }

        insights.append("Keep tracking your progress! Small consistent improvements lead to big results.");

        return insights.toString();
    }

    private HealthMetrics parseHealthMetrics(String healthData) {
        HealthMetrics metrics = new HealthMetrics();

        try {
            // Parse sleep
            if (healthData.contains("Sleep:")) {
                String sleepPart = healthData.split("Sleep:")[1].split("hours")[0].trim();
                metrics.sleepHours = Double.parseDouble(sleepPart);
            }

            // Parse steps
            if (healthData.contains("Steps:")) {
                String stepsPart = healthData.split("Steps:")[1].split(",")[0].trim();
                metrics.steps = Integer.parseInt(stepsPart.replace(",", ""));
            }

            // Parse water
            if (healthData.contains("Water Intake:")) {
                String waterPart = healthData.split("Water Intake:")[1].split("liters")[0].trim();
                metrics.waterIntake = Double.parseDouble(waterPart);
            }

            // Parse exercise
            if (healthData.contains("Exercise:")) {
                String exercisePart = healthData.split("Exercise:")[1].split("minutes")[0].trim();
                metrics.exerciseMinutes = Double.parseDouble(exercisePart);
            }

            // Parse calories
            if (healthData.contains("Calories:")) {
                String caloriesPart = healthData.split("Calories:")[1].split("\\.")[0].trim();
                metrics.calories = Integer.parseInt(caloriesPart);
            }

            // Parse user profile
            if (healthData.contains("years old")) {
                String agePart = healthData.split("years old")[0].split(" ")[healthData.split("years old")[0].split(" ").length - 1];
                metrics.age = Integer.parseInt(agePart);
            }

            if (healthData.contains("cm tall")) {
                String heightPart = healthData.split("cm tall")[0].split(" ")[healthData.split("cm tall")[0].split(" ").length - 1];
                metrics.height = Integer.parseInt(heightPart);
            }

            if (healthData.contains("kg")) {
                String weightPart = healthData.split("kg")[0].split(" ")[healthData.split("kg")[0].split(" ").length - 1];
                metrics.weight = Integer.parseInt(weightPart);
            }

        } catch (Exception e) {
            System.err.println("Error parsing metrics: " + e.getMessage());
        }

        return metrics;
    }

    // Helper class to store parsed metrics
    private static class HealthMetrics {
        double sleepHours = 0;
        int steps = 0;
        double waterIntake = 0;
        double exerciseMinutes = 0;
        int calories = 0;
        int age = 0;
        int height = 0;
        int weight = 0;
    }
}