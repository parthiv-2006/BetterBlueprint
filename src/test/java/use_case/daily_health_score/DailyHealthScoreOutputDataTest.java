package use_case.daily_health_score;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DailyHealthScoreOutputData.
 */
class DailyHealthScoreOutputDataTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 29);
        String userId = "testUser123";
        int score = 85;
        String feedback = "Great job! Keep up the good work.";
        double sleepHours = 7.5;
        double exerciseMinutes = 30.0;
        int calories = 2000;
        double waterIntake = 2.5;
        int steps = 8000;

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, userId, score, feedback, sleepHours, exerciseMinutes, calories, waterIntake, steps
        );

        // Assert
        assertNotNull(outputData, "OutputData should not be null");
        assertEquals(testDate, outputData.getDate(), "Date should match");
        assertEquals(userId, outputData.getUserId(), "User ID should match");
        assertEquals(score, outputData.getScore(), "Score should match");
        assertEquals(feedback, outputData.getFeedback(), "Feedback should match");
        assertEquals(sleepHours, outputData.getSleepHours(), "Sleep hours should match");
        assertEquals(exerciseMinutes, outputData.getExerciseMinutes(), "Exercise minutes should match");
        assertEquals(calories, outputData.getCalories(), "Calories should match");
        assertEquals(waterIntake, outputData.getWaterIntake(), "Water intake should match");
        assertEquals(steps, outputData.getSteps(), "Steps should match");
    }

    @Test
    void testMinimumScore() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        int minScore = 0;

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", minScore, "Needs improvement", 4.0, 0.0, 1000, 0.5, 1000
        );

        // Assert
        assertEquals(minScore, outputData.getScore(), "Should accept minimum score of 0");
    }

    @Test
    void testMaximumScore() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        int maxScore = 100;

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", maxScore, "Perfect!", 8.0, 60.0, 2500, 3.0, 12000
        );

        // Assert
        assertEquals(maxScore, outputData.getScore(), "Should accept maximum score of 100");
    }

    @Test
    void testMidRangeScore() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        int midScore = 75;

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", midScore, "Good work", 7.0, 30.0, 2000, 2.0, 8000
        );

        // Assert
        assertEquals(midScore, outputData.getScore(), "Should accept mid-range score");
    }

    @Test
    void testWithZeroMetrics() {
        // Arrange
        LocalDate testDate = LocalDate.now();

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 20, "Very low activity", 0.0, 0.0, 0, 0.0, 0
        );

        // Assert
        assertEquals(0.0, outputData.getSleepHours(), "Should accept zero sleep hours");
        assertEquals(0.0, outputData.getExerciseMinutes(), "Should accept zero exercise");
        assertEquals(0, outputData.getCalories(), "Should accept zero calories");
        assertEquals(0.0, outputData.getWaterIntake(), "Should accept zero water intake");
        assertEquals(0, outputData.getSteps(), "Should accept zero steps");
    }

    @Test
    void testWithHighMetrics() {
        // Arrange
        LocalDate testDate = LocalDate.now();

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "athlete", 95, "Excellent!", 9.0, 120.0, 3500, 5.0, 20000
        );

        // Assert
        assertEquals(9.0, outputData.getSleepHours(), "Should accept high sleep hours");
        assertEquals(120.0, outputData.getExerciseMinutes(), "Should accept high exercise");
        assertEquals(3500, outputData.getCalories(), "Should accept high calories");
        assertEquals(5.0, outputData.getWaterIntake(), "Should accept high water intake");
        assertEquals(20000, outputData.getSteps(), "Should accept high steps");
    }

    @Test
    void testWithDecimalValues() {
        // Arrange
        LocalDate testDate = LocalDate.now();

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 80, "Good", 7.25, 32.5, 2150, 2.75, 8500
        );

        // Assert
        assertEquals(7.25, outputData.getSleepHours(), 0.001, "Should handle decimal sleep hours");
        assertEquals(32.5, outputData.getExerciseMinutes(), 0.001, "Should handle decimal exercise");
        assertEquals(2.75, outputData.getWaterIntake(), 0.001, "Should handle decimal water intake");
    }

    @Test
    void testWithEmptyFeedback() {
        // Arrange
        LocalDate testDate = LocalDate.now();

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 70, "", 7.0, 30.0, 2000, 2.0, 8000
        );

        // Assert
        assertEquals("", outputData.getFeedback(), "Should accept empty feedback");
    }

    @Test
    void testWithLongFeedback() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        String longFeedback = "This is a very long feedback message that provides detailed information " +
                "about the user's health metrics and suggestions for improvement. " +
                "It includes multiple sentences and various recommendations.";

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 65, longFeedback, 6.5, 25.0, 1800, 1.5, 7000
        );

        // Assert
        assertEquals(longFeedback, outputData.getFeedback(), "Should accept long feedback");
    }

    @Test
    void testDateImmutability() {
        // Arrange
        LocalDate originalDate = LocalDate.of(2025, 11, 29);
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                originalDate, "user", 80, "Good", 7.0, 30.0, 2000, 2.0, 8000
        );

        // Act
        LocalDate retrievedDate = outputData.getDate();
        LocalDate modifiedDate = retrievedDate.plusDays(1);

        // Assert
        assertEquals(originalDate, outputData.getDate(), "Original date should be unchanged");
        assertNotEquals(modifiedDate, outputData.getDate(), "Output data date should be immutable");
    }

    @Test
    void testMultipleInstancesIndependence() {
        // Arrange & Act
        DailyHealthScoreOutputData output1 = new DailyHealthScoreOutputData(
                LocalDate.of(2025, 11, 29), "user1", 85, "Great", 7.5, 30.0, 2000, 2.5, 8000
        );
        DailyHealthScoreOutputData output2 = new DailyHealthScoreOutputData(
                LocalDate.of(2025, 11, 30), "user2", 70, "Good", 6.0, 20.0, 1800, 2.0, 6000
        );

        // Assert
        assertNotEquals(output1.getDate(), output2.getDate(), "Dates should be different");
        assertNotEquals(output1.getUserId(), output2.getUserId(), "User IDs should be different");
        assertNotEquals(output1.getScore(), output2.getScore(), "Scores should be different");
        assertNotEquals(output1.getFeedback(), output2.getFeedback(), "Feedback should be different");
    }

    @Test
    void testRealisticScenario_ExcellentHealth() {
        // Arrange
        LocalDate testDate = LocalDate.now();

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "healthyUser", 95,
                "Outstanding! All your metrics are in the optimal range.",
                8.0, 45.0, 2200, 3.0, 10000
        );

        // Assert
        assertEquals(95, outputData.getScore(), "Excellent health should have high score");
        assertEquals(8.0, outputData.getSleepHours(), "Good sleep hours");
        assertEquals(45.0, outputData.getExerciseMinutes(), "Good exercise duration");
        assertEquals(2200, outputData.getCalories(), "Healthy calorie intake");
        assertEquals(3.0, outputData.getWaterIntake(), "Good water intake");
        assertEquals(10000, outputData.getSteps(), "Good step count");
        assertTrue(outputData.getFeedback().contains("Outstanding"), "Feedback should be positive");
    }

    @Test
    void testRealisticScenario_PoorHealth() {
        // Arrange
        LocalDate testDate = LocalDate.now();

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "needsImprovement", 35,
                "Several areas need attention. Focus on improving sleep and increasing activity.",
                4.5, 5.0, 1200, 0.8, 2000
        );

        // Assert
        assertEquals(35, outputData.getScore(), "Poor health should have low score");
        assertEquals(4.5, outputData.getSleepHours(), "Low sleep hours");
        assertEquals(5.0, outputData.getExerciseMinutes(), "Low exercise duration");
        assertEquals(1200, outputData.getCalories(), "Low calorie intake");
        assertEquals(0.8, outputData.getWaterIntake(), "Low water intake");
        assertEquals(2000, outputData.getSteps(), "Low step count");
        assertTrue(outputData.getFeedback().contains("attention"), "Feedback should mention improvement");
    }

    @Test
    void testRealisticScenario_AverageHealth() {
        // Arrange
        LocalDate testDate = LocalDate.now();

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "averageUser", 72,
                "You're doing well! Try to increase your daily steps.",
                7.0, 25.0, 1950, 2.0, 7500
        );

        // Assert
        assertEquals(72, outputData.getScore(), "Average health should have mid-range score");
        assertEquals(7.0, outputData.getSleepHours(), "Decent sleep hours");
        assertEquals(25.0, outputData.getExerciseMinutes(), "Moderate exercise");
        assertEquals(1950, outputData.getCalories(), "Moderate calorie intake");
        assertEquals(2.0, outputData.getWaterIntake(), "Adequate water intake");
        assertEquals(7500, outputData.getSteps(), "Moderate step count");
        assertTrue(outputData.getFeedback().contains("well"), "Feedback should be encouraging");
    }

    @Test
    void testAllGettersReturnCorrectValues() {
        // Arrange
        LocalDate date = LocalDate.of(2025, 11, 29);
        String userId = "completeTest";
        int score = 88;
        String feedback = "Comprehensive test feedback";
        double sleep = 7.8;
        double exercise = 35.5;
        int calories = 2100;
        double water = 2.8;
        int steps = 9200;

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                date, userId, score, feedback, sleep, exercise, calories, water, steps
        );

        // Assert - Test all getters
        assertEquals(date, outputData.getDate(), "getDate() should return correct value");
        assertEquals(userId, outputData.getUserId(), "getUserId() should return correct value");
        assertEquals(score, outputData.getScore(), "getScore() should return correct value");
        assertEquals(feedback, outputData.getFeedback(), "getFeedback() should return correct value");
        assertEquals(sleep, outputData.getSleepHours(), "getSleepHours() should return correct value");
        assertEquals(exercise, outputData.getExerciseMinutes(), "getExerciseMinutes() should return correct value");
        assertEquals(calories, outputData.getCalories(), "getCalories() should return correct value");
        assertEquals(water, outputData.getWaterIntake(), "getWaterIntake() should return correct value");
        assertEquals(steps, outputData.getSteps(), "getSteps() should return correct value");
    }
}

