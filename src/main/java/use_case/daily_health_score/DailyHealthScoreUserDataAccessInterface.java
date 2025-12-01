package use_case.daily_health_score;

import java.time.LocalDate;
import Entities.HealthMetrics;

public interface DailyHealthScoreUserDataAccessInterface {
    /**
     * Returns the health metrics entity for a given user and date.
     * This is used by the DailyHealthScoreInteractor to retrieve
     * the raw data needed to compute the daily health score.
     *
     * @param userId user identifier
     * @param date date of metrics
     * @return HealthMetrics entity or null if none exist
     */
    HealthMetrics getMetricsForDate(String userId, LocalDate date);

    /**
     * Saves or updates the computed health score for that day.
     * This is optional depending on whether the score should be persisted,
     * but is common in clean architecture to support future features.
     *
     * @param scoreData the output data containing the score & metrics
     */
    void saveDailyHealthScore(DailyHealthScoreOutputData scoreData);

    /**
     * Returns the currently logged-in username.
     *
     * @return the username of the current user
     */
    String getCurrentUsername();
}
