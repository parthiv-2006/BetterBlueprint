package interface_adapter.daily_health_score;

import use_case.daily_health_score.DailyHealthScoreInputBoundary;
import use_case.daily_health_score.DailyHealthScoreInputData;
import use_case.daily_health_score.DailyHealthScoreUserDataAccessInterface;

import java.time.LocalDate;

/**
 * Controller for the DailyHealthScore use case.
 * Collects input from the UI, converts it to an InputData object,
 * and calls the interactor.
 */
public class DailyHealthScoreController {

    private final DailyHealthScoreInputBoundary dailyHealthScoreInteractor;
    private final DailyHealthScoreUserDataAccessInterface userDataAccess;

    public DailyHealthScoreController(DailyHealthScoreInputBoundary interactor,
                                     DailyHealthScoreUserDataAccessInterface userDataAccess) {
        this.dailyHealthScoreInteractor = interactor;
        this.userDataAccess = userDataAccess;
        System.out.println("Controller created with interactor: " + interactor);
    }

    /**
     * Trigger the DailyHealthScore use case.
     * Gets the current user automatically from the data access object.
     *
     * @param date The date for which the health score should be computed.
     */
    public void computeDailyHealthScore(LocalDate date) {
        String userId = userDataAccess.getCurrentUsername();
        System.out.println("Controller: Computing score for userId: " + userId + ", date: " + date);

        DailyHealthScoreInputData inputData =
                new DailyHealthScoreInputData(date, userId);

        dailyHealthScoreInteractor.execute(inputData);
    }
}
