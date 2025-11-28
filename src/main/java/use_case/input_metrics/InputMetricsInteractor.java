package use_case.input_metrics;

import Entities.HealthMetrics;
import java.time.LocalDate;

public class InputMetricsInteractor implements InputMetricsInputBoundary {
    private final InputMetricsDataAccessInterface healthMetricsDataAccess;
    private final InputMetricsOutputBoundary outputBoundary;

    public InputMetricsInteractor(InputMetricsDataAccessInterface healthMetricsDataAccess,
                                  InputMetricsOutputBoundary outputBoundary) {
        this.healthMetricsDataAccess = healthMetricsDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(InputMetricsInputData inputData) {
        try {
            // Get current user ID and date
            String userId = healthMetricsDataAccess.getCurrentUsername();
            LocalDate date = LocalDate.now();

            // Create HealthMetrics with ALL required parameters including steps
            HealthMetrics healthMetrics = new HealthMetrics(
                    userId,
                    date,
                    inputData.getSleepHours(),
                    inputData.getSteps(),
                    inputData.getWaterIntake(),
                    inputData.getExerciseDuration(),
                    inputData.getCalories()
            );

            // Validate metrics
            if (!healthMetrics.validateMetrics()) {
                outputBoundary.prepareFailView("Invalid health metrics provided");
                return;
            }

            // Save metrics
            healthMetricsDataAccess.saveHealthMetrics(healthMetrics);

            // Prepare success response
            InputMetricsOutputData outputData = new InputMetricsOutputData(
                    date.toString(),
                    "Metrics saved successfully",
                    true
            );
            outputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            outputBoundary.prepareFailView("Error saving metrics: " + e.getMessage());
        }
    }
}