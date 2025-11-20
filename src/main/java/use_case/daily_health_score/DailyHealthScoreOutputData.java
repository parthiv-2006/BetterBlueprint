package use_case.daily_health_score;


import Entities.HealthScore;

/**
 * Output Data for the Daily Health Score Use Case.
 */

public class DailyHealthScoreOutputData {
    private final HealthScore healthScore;

    public DailyHealthScoreOutputData(HealthScore healthScore) {
        this.healthScore = healthScore;
    }

    public HealthScore getHealthScore() {
        return healthScore;
    }
}