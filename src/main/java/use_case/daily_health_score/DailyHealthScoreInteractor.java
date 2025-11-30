package use_case.daily_health_score;

import java.time.LocalDate;

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

        // Load existing metrics
        DailyMetricsDTO metrics = userDataAccessObject.getMetricsForDate(userId, date);

        if (metrics == null) {
            healthScorePresenter.prepareFailView(
                    "No health metrics found for " + date + ". Please enter your daily health data first."
            );
            return;
        }

        int score;
        String feedback;


        try {
            // Compute score using Gemini API
            score = scoreCalculator.calculateScore(
                    metrics.getSleepHours(),
                    metrics.getExerciseMinutes(),
                    metrics.getCalories(),
                    metrics.getWaterIntake(),
                    metrics.getSteps()
            );

            // Generate feedback
            feedback = scoreCalculator.generateFeedback(
                    metrics.getSleepHours(),
                    metrics.getExerciseMinutes(),
                    metrics.getCalories(),
                    metrics.getWaterIntake(),
                    metrics.getSteps(),
                    score
            );

        } catch (Exception apiException) {
            // The Gemini service could throw IOException, APIException, InterruptedException, etc.
            healthScorePresenter.prepareFailView(
                    "Failed to generate health score due to an external service error. Please try again later."
            );
            return;
        }

        // Build output data
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                date,
                userId,
                score,
                feedback,
                metrics.getSleepHours(),
                metrics.getExerciseMinutes(),
                metrics.getCalories(),
                metrics.getWaterIntake(),
                metrics.getSteps()
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