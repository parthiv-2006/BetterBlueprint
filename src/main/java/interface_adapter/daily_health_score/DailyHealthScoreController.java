package interface_adapter.daily_health_score;

import Entities.HealthMetrics;
import use_case.daily_health_score.DailyHealthScoreInputBoundary;
import use_case.daily_health_score.DailyHealthScoreInputData;

/**
 * A controller for the DailyHealthScore use case.
 */
public class DailyHealthScoreController {
    private final DailyHealthScoreInputBoundary dailyHealthScoreUseCaseInteractor;

    public DailyHealthScoreController(DailyHealthScoreInputBoundary dailyHealthScoreInteractor) {
        this.dailyHealthScoreUseCaseInteractor = dailyHealthScoreInteractor;
    }

    /**
     * Executes the Daily Health Score Use Case
     */
    public void execute(HealthMetrics healthMetrics) {
        final DailyHealthScoreInputData dailyHealthScoreInputData =
                new DailyHealthScoreInputData(healthMetrics);

        dailyHealthScoreUseCaseInteractor.execute(dailyHealthScoreInputData);

    }
}