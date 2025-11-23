package use_case.daily_health_score;

import Entities.HealthMetrics;
import Entities.HealthScore;
import Entities.User;
import interface_adapter.daily_health_score.GeminiHealthScoreCalculator;

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
    private final GeminiHealthScoreCalculator scoreCalculator;


    // constructor
    public DailyHealthScoreInteractor(DailyHealthScoreUserDataAccessInterface userDataAccessObject,
                                      DailyHealthScoreOutputBoundary outputBoundary,
                                      GeminiHealthScoreCalculator scoreCalculator) {
        this.userDataAccessObject = userDataAccessObject;
        this.healthScorePresenter = outputBoundary;
        this.scoreCalculator = scoreCalculator;
    }


    // execute
    @Override
    public void execute(DailyHealthScoreInputData inputData) {

         //final HealthMetrics healthMetrics = dailyHealthScoreInputData.getHealthMetrics();
         //String userID = inputData.getUserId();
         //LocalDate today = LocalDate.now();
         HealthMetrics healthMetrics = inputData.getHealthMetrics();

         // THIS IS ONLY NECESSARY IF THE INPUT METRICS USE CASE DOESN'T ALREADY CHECK FOR COMPLETENESS
         if (!userDataAccessObject.checkMetricsRecorded(healthMetrics)) {
             healthScorePresenter.prepareFailView("No metrics found for today. Please go to Input Metrics to record today's data.");

         } else {

             try {
                 // calculates score (output data) & passes to presenter

                 int score = scoreCalculator.calculateScore(healthMetrics);
                 String summary = scoreCalculator.generateFeedback(healthMetrics, score);
                 LocalDate date = LocalDate.now();

                 DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                         score,
                         summary,
                         date
                 );

                 healthScorePresenter.prepareSuccessView(outputData);
             }

             catch (Exception e) {
                 healthScorePresenter.prepareFailView("Failed to calculate score using Gemini.");
             }

         }

     }
}