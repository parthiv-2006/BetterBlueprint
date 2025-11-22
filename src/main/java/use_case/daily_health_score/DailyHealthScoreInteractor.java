package use_case.daily_health_score;

import Entities.HealthMetrics;

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
    private final DailyHealthScoreOutputBoundary dailyHealthScorePresenter;   // ?


    // constructor
    public DailyHealthScoreInteractor(DailyHealthScoreUserDataAccessInterface userDataAccessInterface,
                                      DailyHealthScoreOutputBoundary dailyHealthScoreOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.dailyHealthScorePresenter = dailyHealthScoreOutputBoundary;
    }


    // execute
    @Override
    public void execute(DailyHealthScoreInputData inputData) {
        // should use Presenter.prepareFailView & Presenter.prepareSuccessView in here
        // write the logic & what is required for successView & failView

        //final HealthMetrics healthMetrics = dailyHealthScoreInputData.getHealthMetrics();
        String userID = inputData.getUserId();
        LocalDate today = LocalDate.now();
        HealthMetrics healthMetrics = inputData.getHealthMetrics();

        // THIS IS ONLY NECESSARY IF THE INPUT METRICS USE CASE DOESN'T ALREADY CHECK FOR COMPLETENESS
        if (!userDataAccessObject.checkMetricsRecorded(healthMetrics)) {
            dailyHealthScorePresenter.prepareFailView
                    ("Health metrics not available. Please go to the Input Metrics " +
                            "page to record your daily metrics.");
        } else {
            // calculates score (output data) & passes to presenter
            final DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData
                    (userDataAccessObject.calculateHealthScore(inputData.getHealthMetrics()));
            dailyHealthScorePresenter.prepareSuccessView(outputData);
        }

    }
}

/**
 * @Override
 * public void execute(ViewDailyHealthScoreInputData inputData) {
 *
 *     String userId = inputData.getUserId();
 *     LocalDate today = LocalDate.now();
 *
 *     try {
 *         // 1. Retrieve today's metrics
 *         DailyMetrics metrics = userDataAccess.getMetricsForDate(userId, today);
 *
 *         // 2. If no metrics → prompt user to input
 *         if (metrics == null) {
 *             presenter.prepareNoDataView(
 *                     new ViewDailyHealthScoreOutputData(
 *                             null,
 *                             "No metrics entered for today. Please add today's data.",
 *                             false
 *                     )
 *             );
 *             return;
 *         }
 *
 *         // 3. Compute score
 *         int score = scoringAlgorithm.calculateScore(metrics);
 *
 *         // 4. Generate feedback (could also be done in presenter)
 *         String feedback = scoringAlgorithm.generateFeedback(score);
 *
 *         // 5. Return success output data
 *         presenter.prepareSuccessView(
 *                 new ViewDailyHealthScoreOutputData(score, feedback, true)
 *         );
 *
 *     } catch (Exception e) {
 *         // 6. Error flow
 *         presenter.prepareFailView(
 *                 new ViewDailyHealthScoreOutputData(
 *                         null,
 *                         "An error occurred while calculating your health score. Please try again.",
 *                         false
 *                 )
 *         );
 *     }
 * }
 * }
 *
 */
