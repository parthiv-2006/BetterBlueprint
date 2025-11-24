package use_case.daily_health_score;

import Entities.HealthMetrics;
import Entities.HealthScore;

/**
 * DAO interface for the Daily Health Score Use Case.
 */

public interface DailyHealthScoreUserDataAccessInterface {

    /**
     * Checks if all the health metrics are recorded.
     * @param healthMetrics the healthMetrics to check completion of
     */
    boolean checkMetricsRecorded(HealthMetrics healthMetrics);


    /**
     * Calculates health score given the health metrics
     * @param healthMetrics used to calculate the user's daily healthScore
     * @return the HealthScore calculated using healthMetrics
     */
    HealthScore calculateHealthScore(HealthMetrics healthMetrics);


}