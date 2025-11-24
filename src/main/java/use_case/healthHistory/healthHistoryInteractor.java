package use_case.healthHistory;

import Entities.HealthMetrics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        List<HealthMetrics> metrics =
                dataAccess.getHealthMetricsByUser(inputData.getUser());

        LocalDate now = LocalDate.now();
        LocalDate boundary = switch (inputData.getTimeRange().toLowerCase()) {
            case "day" -> now.minusDays(1);
            case "week" -> now.minusWeeks(1);
            case "month" -> now.minusMonths(1);
            case "year" -> now.minusYears(1);
            default -> now.minusYears(20);
        };

        List<LocalDate> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        for (HealthMetrics m : metrics) {
            if (m.getDate().isBefore(boundary)) continue;

            dates.add(m.getDate());
            switch (inputData.getMetricType().toLowerCase()) {
                case "sleep", "sleephours" -> values.add(m.getSleepHours());
                case "water", "waterintake" -> values.add(m.getWaterIntake());
                case "exercise", "exerciseminutes" -> values.add(m.getExerciseMinutes());
                case "calories" -> values.add((double) m.getCalories());
                default -> throw new IllegalArgumentException("Unknown metric: " + inputData.getMetricType());
            }
        }

        List<healthMetricRecord> records = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            records.add(new healthMetricRecord(dates.get(i), values.get(i)));
        }

        presenter.prepareSuccessView(new healthHistoryOutputData(
                inputData.getTimeRange(),
                inputData.getMetricType(),
                records
        ));
    }
}
