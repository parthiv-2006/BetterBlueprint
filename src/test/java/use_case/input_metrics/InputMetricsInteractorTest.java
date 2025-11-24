package use_case.input_metrics;

import Entities.HealthMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
                "testuser", 7.5, 2.0, 2000, 30.0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedMetrics, "Metrics should be saved");
        assertEquals(7.5, mockDataAccess.savedMetrics.getSleepHours());
        assertEquals(2.0, mockDataAccess.savedMetrics.getWaterIntake());
        assertEquals(2000, mockDataAccess.savedMetrics.getCalories());
        assertEquals(30.0, mockDataAccess.savedMetrics.getExerciseMinutes());
    }

    @Test
    void testInvalidSleepHours_Negative() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", -1.0, 2.0, 2000, 30.0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Sleep hours"));
    }

    @Test
    void testInvalidSleepHours_TooHigh() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 25.0, 2.0, 2000, 30.0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled);
        assertTrue(mockPresenter.errorMessage.contains("Sleep hours"));
    }

    @Test
    void testInvalidWaterIntake_Negative() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 7.5, -0.5, 2000, 30.0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled);
        assertTrue(mockPresenter.errorMessage.contains("Water intake"));
    }

    @Test
    void testInvalidCalories_Negative() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 7.5, 2.0, -100, 30.0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled);
        assertTrue(mockPresenter.errorMessage.contains("Calories"));
    }

    @Test
    void testInvalidExerciseDuration_Negative() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 7.5, 2.0, 2000, -10.0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled);
        assertTrue(mockPresenter.errorMessage.contains("Exercise duration"));
    }

    @Test
    void testBoundaryValues_MinimumValid() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 0.0, 0.0, 0, 0.0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled);
        assertNotNull(mockDataAccess.savedMetrics);
    }

    @Test
    void testBoundaryValues_MaximumValid() {
        // Arrange
        InputMetricsInputData inputData = new InputMetricsInputData(
                "testuser", 24.0, 20.0, 10000, 1440.0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled);
        assertNotNull(mockDataAccess.savedMetrics);
    }

    // Mock classes for testing

    private static class MockMetricsDataAccess implements InputMetricsDataAccessInterface {
        HealthMetrics savedMetrics;
        String currentUsername = "testuser";

        @Override
        public void saveHealthMetrics(HealthMetrics healthMetrics) {
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
            // Not tested in these unit tests
        }
    }
}

