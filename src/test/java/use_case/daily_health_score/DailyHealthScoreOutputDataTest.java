package use_case.daily_health_score;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import Entities.HealthMetrics;
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
        HealthMetrics metrics = new HealthMetrics(userId, testDate, 7.5, 8000, 2.5, 30.0, 2000);

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, userId, score, feedback, metrics
        );

        // Assert
        assertNotNull(outputData, "OutputData should not be null");
        assertEquals(testDate, outputData.getDate(), "Date should match");
        assertEquals(userId, outputData.getUserId(), "User ID should match");
        assertEquals(score, outputData.getScore(), "Score should match");
        assertEquals(feedback, outputData.getFeedback(), "Feedback should match");
        assertEquals(7.5, outputData.getMetrics().getSleepHours(), "Sleep hours should match");
        assertEquals(30.0, outputData.getMetrics().getExerciseMinutes(), "Exercise minutes should match");
        assertEquals(2000, outputData.getMetrics().getCalories(), "Calories should match");
        assertEquals(2.5, outputData.getMetrics().getWaterIntake(), "Water intake should match");
        assertEquals(8000, outputData.getMetrics().getSteps(), "Steps should match");
    }

    @Test
    void testMinimumScore() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        HealthMetrics metrics = new HealthMetrics("user", testDate, 4.0, 1000, 0.5, 0.0, 1000);
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 0, "Needs improvement", metrics
        );

        // Assert
        assertEquals(0, outputData.getScore(), "Should accept minimum score of 0");
    }

    @Test
    void testMaximumScore() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        HealthMetrics metrics = new HealthMetrics("user", testDate, 8.0, 12000, 3.0, 60.0, 2500);
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 100, "Perfect!", metrics
        );

        // Assert
        assertEquals(100, outputData.getScore(), "Should accept maximum score of 100");
    }

    @Test
    void testMidRangeScore() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        HealthMetrics metrics = new HealthMetrics("user", testDate, 7.0, 8000, 2.0, 30.0, 2000);
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 75, "Good work", metrics
        );

        // Assert
        assertEquals(75, outputData.getScore(), "Should accept mid-range score");
    }

    @Test
    void testWithZeroMetrics() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        HealthMetrics metrics = new HealthMetrics("user", testDate, 0.0, 0, 0.0, 0.0, 0);

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 20, "Very low activity", metrics
        );

        // Assert
        assertEquals(0.0, outputData.getMetrics().getSleepHours(), "Should accept zero sleep hours");
        assertEquals(0.0, outputData.getMetrics().getExerciseMinutes(), "Should accept zero exercise");
        assertEquals(0, outputData.getMetrics().getCalories(), "Should accept zero calories");
        assertEquals(0.0, outputData.getMetrics().getWaterIntake(), "Should accept zero water intake");
        assertEquals(0, outputData.getMetrics().getSteps(), "Should accept zero steps");
    }

    @Test
    void testWithHighMetrics() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        HealthMetrics metrics = new HealthMetrics("athlete", testDate, 9.0, 20000, 5.0, 120.0, 3500);

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "athlete", 95, "Excellent!", metrics
        );

        // Assert
        assertEquals(9.0, outputData.getMetrics().getSleepHours(), "Should accept high sleep hours");
        assertEquals(120.0, outputData.getMetrics().getExerciseMinutes(), "Should accept high exercise");
        assertEquals(3500, outputData.getMetrics().getCalories(), "Should accept high calories");
        assertEquals(5.0, outputData.getMetrics().getWaterIntake(), "Should accept high water intake");
        assertEquals(20000, outputData.getMetrics().getSteps(), "Should accept high steps");
    }

    @Test
    void testWithDecimalValues() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        HealthMetrics metrics = new HealthMetrics("user", testDate, 7.25, 8500, 2.75, 32.5, 2150);

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 80, "Good", metrics
        );

        // Assert
        assertEquals(7.25, outputData.getMetrics().getSleepHours(), 0.001, "Should handle decimal sleep hours");
        assertEquals(32.5, outputData.getMetrics().getExerciseMinutes(), 0.001, "Should handle decimal exercise");
        assertEquals(2.75, outputData.getMetrics().getWaterIntake(), 0.001, "Should handle decimal water intake");
    }

    @Test
    void testWithEmptyFeedback() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        HealthMetrics metrics = new HealthMetrics("user", testDate, 7.0, 8000, 2.0, 30.0, 2000);

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 70, "", metrics
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
        HealthMetrics metrics = new HealthMetrics("user", testDate, 6.5, 7000, 1.5, 25.0, 1800);

        // Act
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "user", 65, longFeedback, metrics
        );

        // Assert
        assertEquals(longFeedback, outputData.getFeedback(), "Should accept long feedback");
    }

    @Test
    void testDateImmutability() {
        // Arrange
        LocalDate originalDate = LocalDate.of(2025, 11, 29);
        HealthMetrics metrics = new HealthMetrics("user", originalDate, 7.0, 8000, 2.0, 30.0, 2000);
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                originalDate, "user", 80, "Good", metrics
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
                LocalDate.of(2025, 11, 29), "user1", 85,
                "Great", new HealthMetrics("user1", LocalDate.of(2025, 11, 29), 7.5, 8000, 2.5, 30.0, 2000)
        );
        DailyHealthScoreOutputData output2 = new DailyHealthScoreOutputData(
                LocalDate.of(2025, 11, 30), "user2", 70,
                "Good", new HealthMetrics("user2", LocalDate.of(2025, 11, 30), 6.0, 6000, 2.0, 20.0, 1800)
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
        HealthMetrics metrics = new HealthMetrics("healthyUser", testDate, 8.0, 10000, 3.0, 45.0, 2200);
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "healthyUser", 95,
                "Outstanding! All your metrics are in the optimal range.", metrics
        );

        // Assert
        assertEquals(95, outputData.getScore(), "Excellent health should have high score");
        assertEquals(8.0, outputData.getMetrics().getSleepHours(), "Good sleep hours");
        assertEquals(45.0, outputData.getMetrics().getExerciseMinutes(), "Good exercise duration");
        assertEquals(2200, outputData.getMetrics().getCalories(), "Healthy calorie intake");
        assertEquals(3.0, outputData.getMetrics().getWaterIntake(), "Good water intake");
        assertEquals(10000, outputData.getMetrics().getSteps(), "Good step count");
        assertTrue(outputData.getFeedback().contains("Outstanding"), "Feedback should be positive");
    }

    @Test
    void testRealisticScenario_PoorHealth() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        HealthMetrics metrics = new HealthMetrics("needsImprovement", testDate, 4.5, 2000, 0.8, 5.0, 1200);
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "needsImprovement", 35,
                "Several areas need attention. Focus on improving sleep and increasing activity.", metrics
        );

        // Assert
        assertEquals(35, outputData.getScore(), "Poor health should have low score");
        assertEquals(4.5, outputData.getMetrics().getSleepHours(), "Low sleep hours");
        assertEquals(5.0, outputData.getMetrics().getExerciseMinutes(), "Low exercise duration");
        assertEquals(1200, outputData.getMetrics().getCalories(), "Low calorie intake");
        assertEquals(0.8, outputData.getMetrics().getWaterIntake(), "Low water intake");
        assertEquals(2000, outputData.getMetrics().getSteps(), "Low step count");
        assertTrue(outputData.getFeedback().contains("attention"), "Feedback should mention improvement");
    }

    @Test
    void testRealisticScenario_AverageHealth() {
        // Arrange
        LocalDate testDate = LocalDate.now();
        HealthMetrics metrics = new HealthMetrics("averageUser", testDate, 7.0, 7500, 2.0, 25.0, 1950);
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                testDate, "averageUser", 72,
                "You're doing well! Try to increase your daily steps.", metrics
        );

        // Assert
        assertEquals(72, outputData.getScore(), "Average health should have mid-range score");
        assertEquals(7.0, outputData.getMetrics().getSleepHours(), "Decent sleep hours");
        assertEquals(25.0, outputData.getMetrics().getExerciseMinutes(), "Moderate exercise");
        assertEquals(1950, outputData.getMetrics().getCalories(), "Moderate calorie intake");
        assertEquals(2.0, outputData.getMetrics().getWaterIntake(), "Adequate water intake");
        assertEquals(7500, outputData.getMetrics().getSteps(), "Moderate step count");
        assertTrue(outputData.getFeedback().contains("well"), "Feedback should be encouraging");
    }

    @Test
    void testAllGettersReturnCorrectValues() {
        // Arrange
        LocalDate date = LocalDate.of(2025, 11, 29);
        String userId = "completeTest";
        int score = 88;
        String feedback = "Comprehensive test feedback";
        HealthMetrics metrics = new HealthMetrics(userId, date, 7.8, 9200, 2.8, 35.5, 2100);
        DailyHealthScoreOutputData outputData = new DailyHealthScoreOutputData(
                date, userId, score, feedback, metrics
        );

        // Assert - Test all getters
        assertEquals(date, outputData.getDate(), "getDate() should return correct value");
        assertEquals(userId, outputData.getUserId(), "getUserId() should return correct value");
        assertEquals(score, outputData.getScore(), "getScore() should return correct value");
        assertEquals(feedback, outputData.getFeedback(), "getFeedback() should return correct value");
        assertEquals(7.8, outputData.getMetrics().getSleepHours(), "getSleepHours() should return correct value");
        assertEquals(35.5, outputData.getMetrics().getExerciseMinutes(), "getExerciseMinutes() should return correct value");
        assertEquals(2100, outputData.getMetrics().getCalories(), "getCalories() should return correct value");
        assertEquals(2.8, outputData.getMetrics().getWaterIntake(), "getWaterIntake() should return correct value");
        assertEquals(9200, outputData.getMetrics().getSteps(), "getSteps() should return correct value");
    }
}
