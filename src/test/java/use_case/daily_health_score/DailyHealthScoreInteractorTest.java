package use_case.daily_health_score;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import Entities.HealthMetrics;
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
        HealthMetrics metrics = new HealthMetrics(userId, testDate, 7.0, 8000, 2.5, 30.0, 2000);
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
        assertEquals(7.0, outputData.getMetrics().getSleepHours(), "Sleep hours should match");
        assertEquals(30.0, outputData.getMetrics().getExerciseMinutes(), "Exercise minutes should match");
        assertEquals(2000, outputData.getMetrics().getCalories(), "Calories should match");
        assertEquals(2.5, outputData.getMetrics().getWaterIntake(), "Water intake should match");
        assertEquals(8000, outputData.getMetrics().getSteps(), "Steps should match");

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

        HealthMetrics metrics = new HealthMetrics(userId, testDate, 7.0, 8000, 2.5, 30.0, 2000);
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

        HealthMetrics metrics = new HealthMetrics(userId, testDate, 7.0, 8000, 2.5, 30.0, 2000);
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

        HealthMetrics metrics = new HealthMetrics(userId, testDate, 8.5, 10000, 3.0, 45.0, 2500);
        mockDataAccess.setMetricsToReturn(metrics);
        mockCalculator.setScoreToReturn(90);
        mockCalculator.setFeedbackToReturn("Excellent!");

        // Act
        interactor.execute(inputData);

        // Assert
        HealthMetrics passed = mockCalculator.getLastMetrics();
        assertNotNull(passed, "Metrics should be passed to calculator");
        assertEquals(8.5, passed.getSleepHours(), "Sleep hours passed to calculator");
        assertEquals(45.0, passed.getExerciseMinutes(), "Exercise minutes passed to calculator");
        assertEquals(2500, passed.getCalories(), "Calories passed to calculator");
        assertEquals(3.0, passed.getWaterIntake(), "Water intake passed to calculator");
        assertEquals(10000, passed.getSteps(), "Steps passed to calculator");
    }

    // ==================== Mock Classes ====================

    /**
     * Mock implementation of DailyHealthScoreUserDataAccessInterface for testing.
     */
    private static class MockUserDataAccess implements DailyHealthScoreUserDataAccessInterface {
        private HealthMetrics metricsToReturn;
        private boolean saveCalled = false;
        private boolean shouldThrowOnSave = false;
        private DailyHealthScoreOutputData savedData;

        public void setMetricsToReturn(HealthMetrics metrics) {
            this.metricsToReturn = metrics;
        }

        public void setShouldThrowOnSave(boolean shouldThrow) {
            this.shouldThrowOnSave = shouldThrow;
        }

        public boolean isSaveCalled() {
            return saveCalled;
        }

        @Override
        public HealthMetrics getMetricsForDate(String userId, LocalDate date) {
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
        private HealthMetrics lastMetrics;

        public void setScoreToReturn(int score) {
            this.scoreToReturn = score;
        }

        public void setFeedbackToReturn(String feedback) {
            this.feedbackToReturn = feedback;
        }

        public void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }

        public HealthMetrics getLastMetrics() {
            return lastMetrics;
        }

        @Override
        public int calculateScore(HealthMetrics metrics) throws Exception {
            lastMetrics = metrics;

            if (shouldThrowException) {
                throw new Exception("Calculator failed");
            }
            return scoreToReturn;
        }

        @Override
        public String generateFeedback(HealthMetrics metrics, int score) throws Exception {
            if (shouldThrowException) {
                throw new Exception("Feedback generation failed");
            }
            return feedbackToReturn;
        }
    }
}
