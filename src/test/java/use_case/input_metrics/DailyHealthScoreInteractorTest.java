package use_case.input_metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.daily_health_score.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DailyHealthScoreInteractor.
 */
class DailyHealthScoreInteractorTest {

    private DailyHealthScoreInteractor interactor;
    private MockUserDataAccess mockDataAccess;
    private MockPresenter mockPresenter;
    private MockHealthScoreCalculator mockCalculator;

    @BeforeEach
    void setUp() {
        mockDataAccess = new MockUserDataAccess();
        mockPresenter = new MockPresenter();
        mockCalculator = new MockHealthScoreCalculator();
        interactor = new DailyHealthScoreInteractor(mockDataAccess, mockPresenter, mockCalculator);
    }

    @Test
    void testSuccessfulScoreCalculation() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 24);
        String userId = "testUser";
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(testDate, userId);

        // Set up mock data
        DailyMetricsDTO metrics = new DailyMetricsDTO(7.0, 30.0, 2000, 2.5);
        mockDataAccess.setMetricsToReturn(metrics);
        mockCalculator.setScoreToReturn(85);
        mockCalculator.setFeedbackToReturn("Great job! Keep it up.");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessViewCalled(), "Success view should be called");
        assertFalse(mockPresenter.isFailViewCalled(), "Fail view should not be called");

        DailyHealthScoreOutputData outputData = mockPresenter.getOutputData();
        assertNotNull(outputData, "Output data should not be null");
        assertEquals(85, outputData.getScore(), "Score should match");
        assertEquals("Great job! Keep it up.", outputData.getFeedback(), "Feedback should match");
        assertEquals(userId, outputData.getUserId(), "User ID should match");
        assertEquals(testDate, outputData.getDate(), "Date should match");
        assertEquals(7.0, outputData.getSleepHours(), "Sleep hours should match");
        assertEquals(30.0, outputData.getExerciseMinutes(), "Exercise minutes should match");
        assertEquals(2000, outputData.getCalories(), "Calories should match");
        assertEquals(2.5, outputData.getWaterIntake(), "Water intake should match");

        assertTrue(mockDataAccess.isSaveCalled(), "Save should be called");
    }

    @Test
    void testNoMetricsFound() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 24);
        String userId = "testUser";
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(testDate, userId);

        // Set up mock to return null metrics
        mockDataAccess.setMetricsToReturn(null);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailViewCalled(), "Fail view should be called");
        assertFalse(mockPresenter.isSuccessViewCalled(), "Success view should not be called");

        String errorMessage = mockPresenter.getErrorMessage();
        assertNotNull(errorMessage, "Error message should not be null");
        assertTrue(errorMessage.contains("No health metrics found"), "Error message should mention no metrics");
        assertTrue(errorMessage.contains(testDate.toString()), "Error message should include the date");

        assertFalse(mockDataAccess.isSaveCalled(), "Save should not be called when no metrics");
    }

    @Test
    void testCalculatorThrowsException() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 24);
        String userId = "testUser";
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(testDate, userId);

        DailyMetricsDTO metrics = new DailyMetricsDTO(7.0, 30.0, 2000, 2.5);
        mockDataAccess.setMetricsToReturn(metrics);
        mockCalculator.setShouldThrowException(true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailViewCalled(), "Fail view should be called");
        assertFalse(mockPresenter.isSuccessViewCalled(), "Success view should not be called");

        String errorMessage = mockPresenter.getErrorMessage();
        assertNotNull(errorMessage, "Error message should not be null");
        assertTrue(errorMessage.contains("external service error"), "Error message should mention service error");

        assertFalse(mockDataAccess.isSaveCalled(), "Save should not be called when calculator fails");
    }

    @Test
    void testSaveThrowsException() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 24);
        String userId = "testUser";
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(testDate, userId);

        DailyMetricsDTO metrics = new DailyMetricsDTO(7.0, 30.0, 2000, 2.5);
        mockDataAccess.setMetricsToReturn(metrics);
        mockDataAccess.setShouldThrowOnSave(true);
        mockCalculator.setScoreToReturn(85);
        mockCalculator.setFeedbackToReturn("Great job!");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailViewCalled(), "Fail view should be called");
        assertFalse(mockPresenter.isSuccessViewCalled(), "Success view should not be called");

        String errorMessage = mockPresenter.getErrorMessage();
        assertNotNull(errorMessage, "Error message should not be null");
        assertTrue(errorMessage.contains("could not be saved"), "Error message should mention save failure");

        assertTrue(mockDataAccess.isSaveCalled(), "Save should be attempted");
    }

    @Test
    void testCalculatorReceivesCorrectMetrics() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 24);
        String userId = "testUser";
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(testDate, userId);

        DailyMetricsDTO metrics = new DailyMetricsDTO(8.5, 45.0, 2500, 3.0);
        mockDataAccess.setMetricsToReturn(metrics);
        mockCalculator.setScoreToReturn(90);
        mockCalculator.setFeedbackToReturn("Excellent!");

        // Act
        interactor.execute(inputData);

        // Assert
        assertEquals(8.5, mockCalculator.getLastSleepHours(), "Sleep hours passed to calculator");
        assertEquals(45.0, mockCalculator.getLastExerciseMinutes(), "Exercise minutes passed to calculator");
        assertEquals(2500, mockCalculator.getLastCalories(), "Calories passed to calculator");
        assertEquals(3.0, mockCalculator.getLastWaterIntake(), "Water intake passed to calculator");
    }

    // ==================== Mock Classes ====================

    /**
     * Mock implementation of DailyHealthScoreUserDataAccessInterface for testing.
     */
    private static class MockUserDataAccess implements DailyHealthScoreUserDataAccessInterface {
        private DailyMetricsDTO metricsToReturn;
        private boolean saveCalled = false;
        private boolean shouldThrowOnSave = false;
        private DailyHealthScoreOutputData savedData;

        public void setMetricsToReturn(DailyMetricsDTO metrics) {
            this.metricsToReturn = metrics;
        }

        public void setShouldThrowOnSave(boolean shouldThrow) {
            this.shouldThrowOnSave = shouldThrow;
        }

        public boolean isSaveCalled() {
            return saveCalled;
        }

        @Override
        public DailyMetricsDTO getMetricsForDate(String userId, LocalDate date) {
            return metricsToReturn;
        }

        @Override
        public void saveDailyHealthScore(DailyHealthScoreOutputData scoreData) {
            saveCalled = true;
            savedData = scoreData;
            if (shouldThrowOnSave) {
                throw new RuntimeException("Save failed");
            }
        }

        @Override
        public String getCurrentUsername() {
            return "testUser";
        }
    }

    /**
     * Mock implementation of DailyHealthScoreOutputBoundary for testing.
     */
    private static class MockPresenter implements DailyHealthScoreOutputBoundary {
        private boolean successViewCalled = false;
        private boolean failViewCalled = false;
        private DailyHealthScoreOutputData outputData;
        private String errorMessage;

        public boolean isSuccessViewCalled() {
            return successViewCalled;
        }

        public boolean isFailViewCalled() {
            return failViewCalled;
        }

        public DailyHealthScoreOutputData getOutputData() {
            return outputData;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public void prepareSuccessView(DailyHealthScoreOutputData data) {
            successViewCalled = true;
            outputData = data;
        }

        @Override
        public void prepareFailView(String error) {
            failViewCalled = true;
            errorMessage = error;
        }
    }

    /**
     * Mock implementation of HealthScoreCalculator for testing.
     */
    private static class MockHealthScoreCalculator implements HealthScoreCalculator {
        private int scoreToReturn;
        private String feedbackToReturn;
        private boolean shouldThrowException = false;

        private double lastSleepHours;
        private double lastExerciseMinutes;
        private int lastCalories;
        private double lastWaterIntake;

        public void setScoreToReturn(int score) {
            this.scoreToReturn = score;
        }

        public void setFeedbackToReturn(String feedback) {
            this.feedbackToReturn = feedback;
        }

        public void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }

        public double getLastSleepHours() {
            return lastSleepHours;
        }

        public double getLastExerciseMinutes() {
            return lastExerciseMinutes;
        }

        public int getLastCalories() {
            return lastCalories;
        }

        public double getLastWaterIntake() {
            return lastWaterIntake;
        }

        @Override
        public int calculateScore(double sleepHours, double exerciseMinutes, int calories, double waterIntake) throws Exception {
            this.lastSleepHours = sleepHours;
            this.lastExerciseMinutes = exerciseMinutes;
            this.lastCalories = calories;
            this.lastWaterIntake = waterIntake;

            if (shouldThrowException) {
                throw new Exception("Calculator failed");
            }
            return scoreToReturn;
        }

        @Override
        public String generateFeedback(double sleepHours, double exerciseMinutes, int calories, double waterIntake, int score) throws Exception {
            if (shouldThrowException) {
                throw new Exception("Feedback generation failed");
            }
            return feedbackToReturn;
        }
    }
}


