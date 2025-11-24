package interface_adapter.daily_health_score;

import use_case.daily_health_score.DailyHealthScoreInputBoundary;
import use_case.daily_health_score.DailyHealthScoreInputData;

import java.time.LocalDate;

/**
 * Controller for the DailyHealthScore use case.
 * Collects input from the UI, converts it to an InputData object,
 * and calls the interactor.
 */
public class DailyHealthScoreController {

    private final DailyHealthScoreInputBoundary dailyHealthScoreInteractor;

    public DailyHealthScoreController(DailyHealthScoreInputBoundary interactor) {
        this.dailyHealthScoreInteractor = interactor;
        System.out.println("Controller created with interactor: " + interactor);
    }

    /**
     * Trigger the DailyHealthScore use case.
     *
     * @param userId The ID of the user requesting the score.
     * @param date   The date for which the health score should be computed.
     */
    public void computeDailyHealthScore(LocalDate date, String userId) {
        DailyHealthScoreInputData inputData =
                new DailyHealthScoreInputData(date, userId);

        dailyHealthScoreInteractor.execute(inputData);
    }
}
