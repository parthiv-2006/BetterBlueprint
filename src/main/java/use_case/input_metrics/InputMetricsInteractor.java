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
            // Constructor will validate the inputs
            HealthMetrics healthMetrics = new HealthMetrics(
                    userId,
                    date,
                    inputData.getSleepHours(),
                    inputData.getSteps(),
                    inputData.getWaterIntake(),
                    inputData.getExerciseDuration(),
                    inputData.getCalories()
            );

            // Save metrics
            healthMetricsDataAccess.saveHealthMetrics(healthMetrics);

            // Prepare success response
            InputMetricsOutputData outputData = new InputMetricsOutputData(
                    date.toString(),
                    "Metrics saved successfully",
                    true
            );
            outputBoundary.prepareSuccessView(outputData);

        } catch (IllegalArgumentException e) {
            // Handle validation errors from HealthMetrics constructor
            outputBoundary.prepareFailView(e.getMessage());
        } catch (Exception e) {
            outputBoundary.prepareFailView("Error saving metrics: " + e.getMessage());
        }
    }
}