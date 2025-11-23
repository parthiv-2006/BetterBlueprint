package use_case.daily_health_score;

import Entities.HealthMetrics;

public interface HealthScoreCalculator {
    int calculateScore(HealthMetrics metrics) throws Exception;

    // String generateFeedback(int score) throws Exception;

    String generateFeedback(HealthMetrics metrics, int score) throws Exception;
}
