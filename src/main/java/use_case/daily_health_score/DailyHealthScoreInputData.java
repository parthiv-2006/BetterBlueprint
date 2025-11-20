package use_case.daily_health_score;

import Entities.HealthMetrics;

/**
 * The Input Data for the Daily Health Score Use Case.
 */

public class DailyHealthScoreInputData {
    private final HealthMetrics healthMetrics;

    public DailyHealthScoreInputData(HealthMetrics healthMetrics) {
        this.healthMetrics = healthMetrics;
    }

    HealthMetrics getHealthMetrics() {return healthMetrics;}

}