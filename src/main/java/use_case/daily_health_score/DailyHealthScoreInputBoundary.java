package use_case.daily_health_score;

public interface DailyHealthScoreInputBoundary {
    /**
     * Executes the Daily Health Score use case.
     * @param dailyHealthScoreInputData the input data
     */
    void execute(DailyHealthScoreInputData dailyHealthScoreInputData);
}