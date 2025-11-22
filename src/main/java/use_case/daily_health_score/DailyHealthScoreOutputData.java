package use_case.daily_health_score;


import Entities.HealthScore;

/**
 * Output Data for the Daily Health Score Use Case.
 */

public class DailyHealthScoreOutputData {
    private final HealthScore healthScore;
    private final String summary;

    public DailyHealthScoreOutputData(HealthScore healthScore, String summary) {
        this.healthScore = healthScore;
        this.summary = summary;
    }

    public HealthScore getHealthScore() {return healthScore;}

    public String getSummary() {return summary;};

    public String getDate() {};
}