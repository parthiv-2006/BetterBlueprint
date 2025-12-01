package data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import use_case.daily_health_score.DailyHealthScoreOutputData;
import use_case.daily_health_score.DailyHealthScoreUserDataAccessInterface;
import Entities.HealthMetrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * DAO for retrieving raw metrics and saving calculated daily health scores
 * into the same health_metrics.json file used by the Input Metrics use case.
 */
public class DailyHealthScoreDataAccessObject implements DailyHealthScoreUserDataAccessInterface {

    private static final String METRICS_FILE_PATH = "health_metrics.json";
    private final FileUserDataAccessObject userDataAccessObject;

    public DailyHealthScoreDataAccessObject(FileUserDataAccessObject userDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
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

    // return HealthMetrics entity for a given user/date (raw metrics)
    @Override
    public HealthMetrics getMetricsForDate(String userId, LocalDate date) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(METRICS_FILE_PATH)));

            if (content.trim().isEmpty() || content.trim().equals("[]")) {
                return null;
            }

            JSONArray array = new JSONArray(content);
            String dateStr = date.toString();

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                if (obj.getString("userId").equals(userId) &&
                        obj.getString("date").equals(dateStr)) {

                    // raw metrics needed for score calculation
                    double sleepHours = obj.optDouble("sleepHours", 0.0);
                    double exerciseMinutes = obj.optDouble("exerciseMinutes", 0.0);
                    int calories = obj.optInt("calories", 0);
                    double waterIntake = obj.optDouble("waterIntake", 0.0);
                    int steps = obj.optInt("steps", 0);

                    // Map to HealthMetrics entity (constructor expects sleepHour, steps, waterLitres, exerciseMinutes, calories)
                    return new HealthMetrics(userId, date, sleepHours, steps, waterIntake, exerciseMinutes, calories);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading metrics: " + e.getMessage());
        }

        return null;
    }


    // save the computed score + feedback into the same JSON file
    @Override
    public void saveDailyHealthScore(DailyHealthScoreOutputData scoreData) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(METRICS_FILE_PATH)));
            JSONArray array;

            if (content.trim().isEmpty() || content.trim().equals("[]")) {
                array = new JSONArray();
            } else {
                array = new JSONArray(content);
            }

            String dateStr = scoreData.getDate().toString();
            String userId = scoreData.getUserId();

            boolean updatedExisting = false;

            // Update existing record
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                if (obj.getString("userId").equals(userId) &&
                        obj.getString("date").equals(dateStr)) {

                    obj.put("score", scoreData.getScore());
                    obj.put("feedback", scoreData.getFeedback());
                    updatedExisting = true;
                    break;
                }
            }

            // Write back to disk
            try (FileWriter writer = new FileWriter(METRICS_FILE_PATH)) {
                writer.write(array.toString(4)); // pretty print
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to save health score: " + e.getMessage());
        }
    }

    @Override
    public String getCurrentUsername() {
        return userDataAccessObject.getCurrentUsername();
    }
}