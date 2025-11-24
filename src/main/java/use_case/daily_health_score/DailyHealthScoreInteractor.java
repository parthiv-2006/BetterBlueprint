package use_case.daily_health_score;

import java.time.LocalDate;

/**
 * The Daily Health Score Interactor.
 *
 * -------------------------------------------------------------------------------
 * Takes the Input Data and executes the use case, looking up information in the
 * Data Access object when necessary and manipulating Entities.
 * This might create new data that needs to be saved through a Data Access object.
 * When complete, create an Output Data object — the use case result —
 * and pass it to the Presenter.
 * -------------------------------------------------------------------------------
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

        // Step 1: Load existing metrics
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
            // Step 2: Compute score using service (Gemini API)
            score = scoreCalculator.calculateScore(
                    metrics.getSleepHours(),
                    metrics.getExerciseMinutes(),
                    metrics.getCalories(),
                    metrics.getWaterIntake()
            );

            // Step 3: Generate feedback
            feedback = scoreCalculator.generateFeedback(
                    metrics.getSleepHours(),
                    metrics.getExerciseMinutes(),
                    metrics.getCalories(),
                    metrics.getWaterIntake(),
                    score
            );

        } catch (Exception apiException) {
            // The Gemini service could throw IOException, APIException, InterruptedException, etc.
            healthScorePresenter.prepareFailView(
                    "Failed to generate health score due to an external service error. Please try again later."
            );
            return;
        }

        // Step 4: Build output data
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                date,
                userId,
                score,
                feedback,
                metrics.getSleepHours(),
                metrics.getExerciseMinutes(),
                metrics.getCalories(),
                metrics.getWaterIntake()
        );

        // Step 5: Persist the computed score
        try {
            userDataAccessObject.saveDailyHealthScore(outputData);
        } catch (Exception persistenceException) {
            healthScorePresenter.prepareFailView(
                    "Health score calculated but could not be saved. Please try again."
            );
            return;
        }

        // Step 6: Present success
        healthScorePresenter.prepareSuccessView(outputData);
    }
}