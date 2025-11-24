package use_case.daily_health_score;

import java.time.LocalDate;

public interface DailyHealthScoreUserDataAccessInterface {

    /**
     * Returns the health metrics for a given user and date.
     * This is used by the DailyHealthScoreInteractor to retrieve
     * the raw data needed to compute the daily health score.
     *
     * @param userId the ID of the user
     * @param date the date of the metrics requested
     * @return a DailyMetricsDTO containing the raw health metrics,
     *         or null if no data exists for that date.
     */
    DailyMetricsDTO getMetricsForDate(String userId, LocalDate date);


    /**
     * Saves or updates the computed health score for that day.
     * This is optional depending on whether the score should be persisted,
     * but is common in clean architecture to support future features.
     *
     * @param scoreData the output data containing the score & metrics
     */
    void saveDailyHealthScore(DailyHealthScoreOutputData scoreData);
}
