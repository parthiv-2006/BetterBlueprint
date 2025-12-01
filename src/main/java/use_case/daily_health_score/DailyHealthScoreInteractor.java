package use_case.daily_health_score;

import java.time.LocalDate;
import Entities.HealthMetrics;

/**
 * The Daily Health Score Interactor.
 */

public class DailyHealthScoreInteractor implements DailyHealthScoreInputBoundary {

    private final DailyHealthScoreUserDataAccessInterface userDataAccessObject;
    private final DailyHealthScoreOutputBoundary healthScorePresenter;
    private final HealthScoreCalculator scoreCalculator;

    public DailyHealthScoreInteractor(DailyHealthScoreUserDataAccessInterface userDataAccessObject,
                                      DailyHealthScoreOutputBoundary outputBoundary,
                                      HealthScoreCalculator scoreCalculator) {
        this.userDataAccessObject = userDataAccessObject;
        this.healthScorePresenter = outputBoundary;
        this.scoreCalculator = scoreCalculator;
    }

    @Override
    public void execute(DailyHealthScoreInputData inputData) {

        String userId = inputData.getUserId();
        LocalDate date = inputData.getDate();

        // Load existing metrics directly as entity
        HealthMetrics healthMetrics = userDataAccessObject.getMetricsForDate(userId, date);

        if (healthMetrics == null) {
            healthScorePresenter.prepareFailView(
                    "No health metrics found for " + date + ". Please enter your daily health data first."
            );
            return;
        }


        int score;
        String feedback;


        try {
            // Compute score using Gemini API via calculator
            score = scoreCalculator.calculateScore(healthMetrics);

            // Generate feedback
            feedback = scoreCalculator.generateFeedback(healthMetrics, score);

        } catch (Exception apiException) {
            // The Gemini service could throw IOException, APIException, InterruptedException, etc.
            healthScorePresenter.prepareFailView(
                    "Failed to generate health score due to an external service error. Please try again later."
            );
            return;
        }

        // Build output data (still uses raw primitives for presentation/reporting)
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                date,
                userId,
                score,
                feedback,
                healthMetrics
        );

        // Persist the computed score
        try {
            userDataAccessObject.saveDailyHealthScore(outputData);
        } catch (Exception persistenceException) {
            healthScorePresenter.prepareFailView(
                    "Health score calculated but could not be saved. Please try again."
            );
            return;
        }

        // Present success
        healthScorePresenter.prepareSuccessView(outputData);
    }
}