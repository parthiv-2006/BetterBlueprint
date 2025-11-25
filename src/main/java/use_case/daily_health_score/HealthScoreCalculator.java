package use_case.daily_health_score;

/**
 * Interface for calculating health scores and generating feedback.
 */

public interface HealthScoreCalculator {
    int calculateScore(double sleepHours,
                       double exerciseMinutes,
                       int calories,
                       double waterIntake) throws Exception;

    String generateFeedback(double sleepHours,
                            double exerciseMinutes,
                            int calories,
                            double waterIntake,
                            int score) throws Exception;
}
