package use_case.input_metrics;

import Entities.HealthMetrics;

import java.time.LocalDate;

/**
 * The Input Metrics Interactor.
 */
public class InputMetricsInteractor implements InputMetricsInputBoundary {
    private final InputMetricsDataAccessInterface metricsDataAccess;
    private final InputMetricsOutputBoundary presenter;

    public InputMetricsInteractor(InputMetricsDataAccessInterface metricsDataAccess,
                                  InputMetricsOutputBoundary presenter) {
        this.metricsDataAccess = metricsDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(InputMetricsInputData inputData) {
        // Validate sleep hours
        if (inputData.getSleepHours() < 0 || inputData.getSleepHours() > 24) {
            presenter.prepareFailView("Sleep hours must be between 0 and 24 hours.");
            return;
        }

        // Validate water intake
        if (inputData.getWaterIntake() < 0 || inputData.getWaterIntake() > 20) {
            presenter.prepareFailView("Water intake must be between 0 and 20 liters.");
            return;
        }

        // Validate calories
        if (inputData.getCaloriesConsumed() < 0 || inputData.getCaloriesConsumed() > 10000) {
            presenter.prepareFailView("Calories must be between 0 and 10000.");
            return;
        }

        // Validate exercise duration
        if (inputData.getExerciseDuration() < 0 || inputData.getExerciseDuration() > 1440) {
            presenter.prepareFailView("Exercise duration must be between 0 and 1440 minutes (24 hours).");
            return;
        }

        try {
            // Get current username
            String username = inputData.getUsername();
            if (username == null || username.isEmpty()) {
                username = metricsDataAccess.getCurrentUsername();
            }

            // Create health metrics entity
            LocalDate today = LocalDate.now();
            HealthMetrics metrics = new HealthMetrics(
                    username,
                    today,
                    inputData.getSleepHours(),
                    inputData.getWaterIntake(),
                    inputData.getExerciseDuration(),
                    inputData.getCaloriesConsumed()
            );

            // Save to data access
            metricsDataAccess.saveHealthMetrics(metrics);

            // Prepare success view
            InputMetricsOutputData outputData = new InputMetricsOutputData(username, today, true);
            presenter.prepareSuccessView(outputData);

        } catch (IllegalArgumentException e) {
            presenter.prepareFailView(e.getMessage());
        } catch (Exception e) {
            presenter.prepareFailView("Failed to save health metrics: " + e.getMessage());
        }
    }

    @Override
    public void switchToHomeView() {
        presenter.switchToHomeView();
    }
}

