package interface_adapter.daily_health_score;

import use_case.daily_health_score.HealthScoreCalculator;

/**
 * A simple fallback implementation of HealthScoreCalculator that doesn't require API key.
 * Uses basic heuristics to calculate health scores.
 */
public class SimpleHealthScoreCalculator implements HealthScoreCalculator {

    @Override
    public int calculateScore(double sleepHours,
                              double exerciseMinutes,
                              int calories,
                              double waterIntake,
                              int steps) throws Exception {
        int score = 0;
        score += calculateSleepScore(sleepHours);
        score += calculateExerciseScore(exerciseMinutes);
        score += calculateCalorieScore(calories);
        score += calculateWaterScore(waterIntake);
        score += calculateStepsScore(steps);
        return Math.min(score, 100); // Cap at 100
    }

    private int calculateSleepScore(double sleepHours) {
        if (sleepHours >= 7 && sleepHours <= 9) {
            return 25;
        } else if (sleepHours >= 6 && sleepHours < 10) {
            return 15;
        } else if (sleepHours > 0) {
            return 5;
        }
        return 0;
    }

    private int calculateExerciseScore(double exerciseMinutes) {
        if (exerciseMinutes >= 30 && exerciseMinutes <= 60) {
            return 25;
        } else if (exerciseMinutes >= 20 && exerciseMinutes < 90) {
            return 15;
        } else if (exerciseMinutes > 0) {
            return 5;
        }
        return 0;
    }

    private int calculateCalorieScore(int calories) {
        if (calories >= 1500 && calories <= 2500) {
            return 25;
        } else if (calories >= 1000 && calories < 3000) {
            return 15;
        } else if (calories > 0) {
            return 5;
        }
        return 0;
    }

    private int calculateWaterScore(double waterIntake) {
        if (waterIntake >= 2.0) {
            return 25;
        } else if (waterIntake >= 1.5) {
            return 15;
        } else if (waterIntake > 0) {
            return 5;
        }
        return 0;
    }

    private int calculateStepsScore(int steps) {
        if (steps >= 8000) {
            return 20;
        } else if (steps >= 5000) {
            return 15;
        } else if (steps >= 3000) {
            return 10;
        } else if (steps > 0) {
            return 5;
        }
        return 0;
    }

    @Override
    public String generateFeedback(double sleepHours,
                                   double exerciseMinutes,
                                   int calories,
                                   double waterIntake,
                                   int steps,
                                   int score) throws Exception {
        StringBuilder feedback = new StringBuilder();
        feedback.append("Health Score: ").append(score).append("/100\n\n");
        feedback.append(getSleepFeedback(sleepHours));
        feedback.append(getExerciseFeedback(exerciseMinutes));
        feedback.append(getCalorieFeedback(calories));
        feedback.append(getWaterFeedback(waterIntake));
        feedback.append(getStepsFeedback(steps));
        return feedback.toString();
    }

    private String getSleepFeedback(double sleepHours) {
        if (sleepHours >= 7 && sleepHours <= 9) {
            return "✓ Great sleep! You're getting optimal rest.\n";
        } else if (sleepHours < 7) {
            return "⚠ Try to get more sleep. Aim for 7-9 hours.\n";
        } else {
            return "⚠ You might be sleeping too much. Aim for 7-9 hours.\n";
        }
    }

    private String getExerciseFeedback(double exerciseMinutes) {
        if (exerciseMinutes >= 30 && exerciseMinutes <= 60) {
            return "✓ Excellent exercise routine!\n";
        } else if (exerciseMinutes < 30) {
            return "⚠ Try to exercise more. Aim for at least 30 minutes.\n";
        } else {
            return "✓ Great activity level!\n";
        }
    }

    private String getCalorieFeedback(int calories) {
        if (calories >= 1500 && calories <= 2500) {
            return "✓ Good caloric intake!\n";
        } else if (calories < 1500) {
            return "⚠ Consider eating more. You might not be getting enough calories.\n";
        } else {
            return "⚠ Consider eating less. You're exceeding typical caloric needs.\n";
        }
    }

    private String getWaterFeedback(double waterIntake) {
        if (waterIntake >= 2.0) {
            return "✓ Excellent hydration!\n";
        } else {
            return "⚠ Drink more water. Aim for at least 2 liters (8+ glasses).\n";
        }
    }

    private String getStepsFeedback(int steps) {
        if (steps >= 8000) {
            return "✓ Great job! You're hitting your step goals!\n";
        } else if (steps >= 5000) {
            return "⚠ Good effort! Try to reach 8,000+ steps daily.\n";
        } else {
            return "⚠ Try to be more active. Aim for at least 8,000 steps per day.\n";
        }
    }
}

