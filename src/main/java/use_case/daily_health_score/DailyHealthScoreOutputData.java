package use_case.daily_health_score;


import Entities.healthScore;

/**
 * Output Data for the Daily Health Score Use Case.
 */

public class DailyHealthScoreOutputData {
    private final healthScore healthScore;

    public DailyHealthScoreOutputData(healthScore healthScore) {
        this.healthScore = healthScore;
    }

    public healthScore getHealthScore() {
        return healthScore;
    }
}