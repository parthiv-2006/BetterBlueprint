package use_case.healthHistory;

import Entities.HealthMetrics;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;


/**
 * Interactor for viewing health score history.
 */
public class healthHistoryInteractor implements healthHistoryInputBoundary {

    private final healthHistoryUserDataAccessInterface dataAccess;
    private final healthHistoryOutputBoundary presenter;

    public healthHistoryInteractor(healthHistoryUserDataAccessInterface dataAccess,
                                   healthHistoryOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(healthHistoryInputData inputData) {
        fetchHistory(inputData.getMetricType(), inputData.getTimeRange(), inputData.getUser());
    }

    /**
     * Fetch history for a metric/timeRange/user.
     */
    public void fetchHistory(String metricType, String timeRange, String userId) {
        List<healthMetricRecord> records = new ArrayList<>();

        // === Fixed: ensure the dataAccess branch is a proper block ===
        if (dataAccess != null) {
            List<HealthMetrics> metrics = dataAccess.getHealthMetricsByUser(userId);
            if (metrics == null || metrics.isEmpty()) {
                presenter.prepareSuccessView(new healthHistoryOutputData(timeRange, metricType, records));
                return;
            }

            LocalDate now = LocalDate.now();
            LocalDate boundary = switch (timeRange.toLowerCase()) {
                case "day" -> now.minusDays(1);
                case "week" -> now.minusWeeks(1);
                case "month" -> now.minusMonths(1);
                case "year" -> now.minusYears(1);
                default -> now.minusYears(20);
            };

            for (HealthMetrics m : metrics) {
                if (m.getDate().isBefore(boundary)) continue;

                double val;
                switch (metricType.toLowerCase()) {
                    case "sleep", "sleephours" -> val = m.getSleepHour();
                    case "water", "waterintake" -> val = m.getWaterLitres();
                    case "exercise", "exerciseminutes" -> val = m.getExerciseMinutes();
                    case "calories" -> val = (double) m.getCalories();
                    default -> {
                        // Unknown metric: skip this metric entry rather than throw.
                        continue;
                    }
                }
                records.add(new healthMetricRecord(m.getDate(), val));
            }

            presenter.prepareSuccessView(new healthHistoryOutputData(timeRange, metricType, records));
            return;
        }

        // ...existing JSON fallback code unchanged...
        String content = null;
        try (InputStream is = getClass().getResourceAsStream("/health_metrics.json")) {
            if (is != null) {
                content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            } else {
                try {
                    content = Files.readString(Paths.get("health_metrics.json"), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    content = null;
                }
            }
        } catch (IOException ignored) {
        }

        if (content == null || content.isBlank()) {
            // return empty success (presenter will handle empty list)
            presenter.prepareSuccessView(new healthHistoryOutputData(timeRange, metricType, records));
            return;
        }

        // tolerant object matcher
        Pattern objPattern = Pattern.compile("\\{[^}]*\\}");
        Matcher objMatcher = objPattern.matcher(content);

        DateTimeFormatter isoFmt = DateTimeFormatter.ISO_LOCAL_DATE;

        // figure likely metric key
        String metricKey = metricType;
        if (metricType.equalsIgnoreCase("sleep")) metricKey = "sleepHours";
        if (metricType.equalsIgnoreCase("water")) metricKey = "waterIntake";
        if (metricType.equalsIgnoreCase("exercise")) metricKey = "exerciseMinutes";

        Pattern datePattern = Pattern.compile("\"date\"\\s*:\\s*\"([^\"]+)\"");
        Pattern metricPattern = Pattern.compile("\"" + Pattern.quote(metricKey) + "\"\\s*:\\s*([0-9]+\\.?[0-9]*)");
        Pattern userPattern = Pattern.compile("\"user\"\\s*:\\s*\"([^\"]+)\"");

        List<Record> temp = new ArrayList<>();

        while (objMatcher.find()) {
            String obj = objMatcher.group();
            Matcher userMatcher = userPattern.matcher(obj);
            if (userMatcher.find()) {
                String u = userMatcher.group(1);
                if (!u.equals(userId)) continue;
            }

            Matcher dMatch = datePattern.matcher(obj);
            Matcher mMatch = metricPattern.matcher(obj);

            if (dMatch.find() && mMatch.find()) {
                try {
                    LocalDate date = LocalDate.parse(dMatch.group(1), isoFmt);
                    double val = Double.parseDouble(mMatch.group(1));
                    temp.add(new Record(date, val));
                } catch (Exception ignored) {
                }
            }
        }

        if (temp.isEmpty()) {
            presenter.prepareSuccessView(new healthHistoryOutputData(timeRange, metricType, records));
            return;
        }

        LocalDate now = LocalDate.now();
        LocalDate boundary = switch (timeRange.toLowerCase()) {
            case "day" -> now.minusDays(1);
            case "week" -> now.minusWeeks(1);
            case "month" -> now.minusMonths(1);
            case "year" -> now.minusYears(1);
            default -> now.minusYears(20);
        };

        List<Record> filtered = temp.stream()
                .filter(r -> !r.date.isBefore(boundary))
                .sorted(Comparator.comparing(r -> r.date))
                .collect(Collectors.toList());

        for (Record r : filtered) {
            records.add(new healthMetricRecord(r.date, r.value));
        }

        presenter.prepareSuccessView(new healthHistoryOutputData(timeRange, metricType, records));
    }

    private static class Record {
        LocalDate date;
        double value;
        Record(LocalDate date, double value) { this.date = date; this.value = value; }
    }
}
