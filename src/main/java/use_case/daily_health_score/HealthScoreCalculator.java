package use_case.daily_health_score;

import Entities.HealthMetrics;

/**
 * Interface for calculating health scores and generating feedback using a HealthMetrics entity.
 */

public interface HealthScoreCalculator {
    int calculateScore(HealthMetrics metrics) throws Exception;

    String generateFeedback(HealthMetrics metrics, int score) throws Exception;
}
