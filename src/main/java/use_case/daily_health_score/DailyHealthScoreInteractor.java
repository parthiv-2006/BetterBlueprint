package use_case.daily_health_score;

import Entities.HealthMetrics;

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
    public void execute(DailyHealthScoreInputData dailyHealthScoreInputData) {
        // should use Presenter.prepareFailView & Presenter.prepareSuccessView in here
        // write the logic & what is required for successView & failView
        final HealthMetrics healthMetrics = dailyHealthScoreInputData.getHealthMetrics();

        // THIS IS ONLY NECESSARY IF THE INPUT METRICS USE CASE DOESN'T ALREADY CHECK FOR COMPLETENESS
        if (!userDataAccessObject.checkMetricsRecorded(healthMetrics)) {
            dailyHealthScorePresenter.prepareFailView
                    ("Health metrics not available. Please go to the Input Metrics " +
                            "page to record your daily metrics.");
        } else {
            // calculates score (output data) & passes to presenter
            final DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData
                    (userDataAccessObject.calculateHealthScore(dailyHealthScoreInputData.getHealthMetrics()));
            dailyHealthScorePresenter.prepareSuccessView(outputData);
        }

    }
}