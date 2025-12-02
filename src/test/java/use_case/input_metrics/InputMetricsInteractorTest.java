package use_case.input_metrics;

import Entities.HealthMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the InputMetricsInteractor.
 */
class InputMetricsInteractorTest {

    private MockMetricsDataAccess mockDataAccess;
    private MockPresenter mockPresenter;
    private InputMetricsInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDataAccess = new MockMetricsDataAccess();
        mockPresenter = new MockPresenter();
        interactor = new InputMetricsInteractor(mockDataAccess, mockPresenter);
    }

    @Test
    void testValidMetrics() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 7.5f, 8000, 2.0f, 2000, 30.0f);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedMetrics, "Metrics should be saved");
        assertEquals(7.5, mockDataAccess.savedMetrics.getSleepHours());
        assertEquals(8000, mockDataAccess.savedMetrics.getSteps());
        assertEquals(2.0, mockDataAccess.savedMetrics.getWaterIntake());
        assertEquals(2000, mockDataAccess.savedMetrics.getCalories());
        assertEquals(30.0, mockDataAccess.savedMetrics.getExerciseMinutes());
        assertNotNull(mockPresenter.outputData, "Output data should be set");
        assertTrue(mockPresenter.outputData.isSuccess(), "Output should indicate success");
        assertEquals("Metrics saved successfully", mockPresenter.outputData.getMessage());
    }

    @Test
    void testInvalidSleepHours_Negative() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", -1.0f, 8000, 2.0f, 2000, 30.0f);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Sleep hours") ||
                   mockPresenter.errorMessage.contains("sleep"),
                   "Error message should mention sleep hours");
    }

    @Test
    void testInvalidSleepHours_TooHigh() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 25.0f, 8000, 2.0f, 2000, 30.0f);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Sleep hours") ||
                   mockPresenter.errorMessage.contains("sleep"),
                   "Error message should mention sleep hours");
    }

    @Test
    void testInvalidSteps_Negative() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 7.5f, -100, 2.0f, 2000, 30.0f);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Steps") ||
                   mockPresenter.errorMessage.contains("steps"),
                   "Error message should mention steps");
    }

    @Test
    void testInvalidWaterIntake_Negative() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 7.5f, 8000, -0.5f, 2000, 30.0f);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Water intake") ||
                   mockPresenter.errorMessage.contains("Water") ||
                   mockPresenter.errorMessage.contains("water"),
                   "Error message should mention water intake");
    }

    @Test
    void testInvalidCalories_Negative() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 7.5f, 8000, 2.0f, -100, 30.0f);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Calories") ||
                   mockPresenter.errorMessage.contains("calories"),
                   "Error message should mention calories");
    }

    @Test
    void testInvalidExerciseDuration_Negative() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 7.5f, 8000, 2.0f, 2000, -10.0f);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Exercise duration") ||
                   mockPresenter.errorMessage.contains("Exercise") ||
                   mockPresenter.errorMessage.contains("exercise"),
                   "Error message should mention exercise duration");
    }

    @Test
    void testBoundaryValues_MinimumValid() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 0.0f, 0, 0.0f, 0, 0.0f);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedMetrics, "Metrics should be saved");
        assertEquals(0.0, mockDataAccess.savedMetrics.getSleepHours());
        assertEquals(0, mockDataAccess.savedMetrics.getSteps());
        assertEquals(0.0, mockDataAccess.savedMetrics.getWaterIntake());
        assertEquals(0, mockDataAccess.savedMetrics.getCalories());
        assertEquals(0.0, mockDataAccess.savedMetrics.getExerciseMinutes());
    }

    @Test
    void testBoundaryValues_MaximumValid() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 24.0f, 50000, 20.0f, 10000, 1440.0f);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedMetrics, "Metrics should be saved");
        assertEquals(24.0, mockDataAccess.savedMetrics.getSleepHours());
        assertEquals(50000, mockDataAccess.savedMetrics.getSteps());
        assertEquals(20.0, mockDataAccess.savedMetrics.getWaterIntake());
        assertEquals(10000, mockDataAccess.savedMetrics.getCalories());
        assertEquals(1440.0, mockDataAccess.savedMetrics.getExerciseMinutes());
    }

    @Test
    void testDataAccessException() {
        // Arrange
        mockDataAccess.shouldThrowException = true;
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 7.5f, 8000, 2.0f, 2000, 30.0f);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Error saving metrics"));
    }

    @Test
    void testInputDataGetters() {
        // This test ensures all getters in InputMetricsInputData are covered
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 7.5f, 8000, 2.0f, 2000, 30.0f);

        // Call all getters to achieve 100% coverage
        assertEquals("testuser", inputData.getUsername());
        assertEquals(7.5f, inputData.getSleepHours());
        assertEquals(8000, inputData.getSteps());
        assertEquals(2.0f, inputData.getWaterIntake());
        assertEquals(2000, inputData.getCalories());
        assertEquals(30.0f, inputData.getExerciseDuration());
    }

    @Test
    void testOutputDataGetters() {
        // This test ensures all getters in InputMetricsOutputData are covered
        InputMetricsOutputData outputData = new InputMetricsOutputData(
                "2024-12-02", "Test message", true);

        // Call all getters to achieve 100% coverage
        assertEquals("2024-12-02", outputData.getDate());
        assertEquals("Test message", outputData.getMessage());
        assertTrue(outputData.isSuccess());

        // Test with false success value
        InputMetricsOutputData failOutputData = new InputMetricsOutputData(
                "2024-12-02", "Fail message", false);
        assertFalse(failOutputData.isSuccess());
    }

    // Mock classes for testing

    private static class MockMetricsDataAccess implements InputMetricsDataAccessInterface {
        HealthMetrics savedMetrics;
        String currentUsername = "testuser";
        boolean shouldThrowException = false;

        @Override
        public void saveHealthMetrics(HealthMetrics healthMetrics) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }
            this.savedMetrics = healthMetrics;
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }
    }

    private static class MockPresenter implements InputMetricsOutputBoundary {
        boolean isSuccessCalled = false;
        boolean isFailCalled = false;
        String errorMessage = "";
        InputMetricsOutputData outputData;

        @Override
        public void prepareSuccessView(InputMetricsOutputData outputData) {
            this.isSuccessCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.isFailCalled = true;
            this.errorMessage = errorMessage;
        }

        @Override
        public void switchToHomeView() {
            // Not implemented in InputMetricsInteractor
        }
    }
}

