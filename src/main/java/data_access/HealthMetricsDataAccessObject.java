package data_access;

import Entities.HealthMetrics;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.input_metrics.InputMetricsDataAccessInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for storing health metrics in JSON format.
 */
public class HealthMetricsDataAccessObject implements InputMetricsDataAccessInterface, HealthDataAccessInterface {
    private static final String METRICS_FILE_PATH = "health_metrics.json";
    private final FileUserDataAccessObject userDataAccessObject;

    public HealthMetricsDataAccessObject(FileUserDataAccessObject userDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;

        // Create file if it doesn't exist
        File file = new File(METRICS_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
            } catch (IOException e) {
                throw new RuntimeException("Error creating health metrics file: " + e.getMessage());
            }
        }
    }

    @Override
    public void saveHealthMetrics(HealthMetrics healthMetrics) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(METRICS_FILE_PATH)));
            JSONArray metricsArray;

            if (content.trim().isEmpty() || content.trim().equals("[]")) {
                metricsArray = new JSONArray();
            } else {
                metricsArray = new JSONArray(content);
            }

            // Check if metrics for this user and date already exist
            boolean updated = false;
            for (int i = 0; i < metricsArray.length(); i++) {
                JSONObject obj = metricsArray.getJSONObject(i);
                if (obj.getString("userId").equals(healthMetrics.getUserId()) &&
                        obj.getString("date").equals(healthMetrics.getDate().toString())) {
                    // Update existing entry
                    metricsArray.put(i, healthMetricsToJson(healthMetrics));
                    updated = true;
                    break;
                }
            }

            // If not updated, add new entry
            if (!updated) {
                metricsArray.put(healthMetricsToJson(healthMetrics));
            }

            // Write back to file
            try (FileWriter writer = new FileWriter(METRICS_FILE_PATH)) {
                writer.write(metricsArray.toString(4));
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to save health metrics: " + e.getMessage());
        }
    }

    @Override
    public List<HealthMetrics> getHealthMetricsByUser(String userId) {
        List<HealthMetrics> userMetrics = new ArrayList<>();

        try {
            String content = new String(Files.readAllBytes(Paths.get(METRICS_FILE_PATH)));

            if (content.trim().isEmpty() || content.trim().equals("[]")) {
                return userMetrics;
            }

            JSONArray metricsArray = new JSONArray(content);

            for (int i = 0; i < metricsArray.length(); i++) {
                JSONObject obj = metricsArray.getJSONObject(i);
                if (obj.getString("userId").equals(userId)) {
                    userMetrics.add(jsonToHealthMetrics(obj));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading health metrics: " + e.getMessage());
        }

        return userMetrics;
    }

    @Override
    public String getCurrentUsername() {
        return userDataAccessObject.getCurrentUsername();
    }

    /**
     * Converts HealthMetrics object to JSON.
     */
    private JSONObject healthMetricsToJson(HealthMetrics metrics) {
        JSONObject json = new JSONObject();
        json.put("userId", metrics.getUserId());
        json.put("date", metrics.getDate().toString());

        // NEW field names (for your Health Insights)
        json.put("sleepHour", metrics.getSleepHour());
        json.put("steps", metrics.getSteps());
        json.put("waterLitres", metrics.getWaterLitres());
        json.put("exerciseMinutes", metrics.getExerciseMinutes());
        json.put("calories", metrics.getCalories());

        // OLD field names (for your teammate's Health Score - MAINTAIN COMPATIBILITY)
        json.put("sleepHours", metrics.getSleepHour());        // Same value as sleepHour
        json.put("waterIntake", metrics.getWaterLitres());     // Same value as waterLitres

        return json;
    }

    /**
     * Converts JSON to HealthMetrics object.
     */
    private HealthMetrics jsonToHealthMetrics(JSONObject json) {
        // Handle both old and new field names for backward compatibility
        double sleepHour;
        if (json.has("sleepHour")) {
            sleepHour = json.getDouble("sleepHour");
        } else if (json.has("sleepHours")) {
            sleepHour = json.getDouble("sleepHours"); // Fallback for old data
        } else {
            sleepHour = 0.0;
        }

        int steps = json.optInt("steps", 0);

        double waterLitres;
        if (json.has("waterLitres")) {
            waterLitres = json.getDouble("waterLitres");
        } else if (json.has("waterIntake")) {
            waterLitres = json.getDouble("waterIntake"); // Fallback for old data
        } else {
            waterLitres = 0.0;
        }

        double exerciseMinutes = json.optDouble("exerciseMinutes", 0.0);
        int calories = json.optInt("calories", 0);

        return new HealthMetrics(
                json.getString("userId"),
                LocalDate.parse(json.getString("date")),
                sleepHour,
                steps,
                waterLitres,
                exerciseMinutes,
                calories
        );
    }

    public HealthMetrics getLatestMetrics(String userId) {
        List<HealthMetrics> allMetrics = getHealthMetricsByUser(userId);

        if (allMetrics.isEmpty()) {
            return null;
        }

        // Find the most recent entry
        HealthMetrics latest = allMetrics.get(0);
        for (HealthMetrics m : allMetrics) {
            if (m.getDate().isAfter(latest.getDate())) {
                latest = m;
            }
        }

        return latest;
    }

}