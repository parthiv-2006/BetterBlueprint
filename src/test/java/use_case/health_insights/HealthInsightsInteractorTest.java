package use_case.health_insights;

import Entities.HealthMetrics;
import Entities.User;
import data_access.HealthDataAccessInterface;
import data_access.UserDataAccessInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.GeminiAPIService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HealthInsightsInteractorTest {

    private HealthInsightsInteractor interactor;
    private MockOutputBoundary mockOutputBoundary;
    private MockHealthDataAccess mockHealthDataAccess;
    private MockUserDataAccess mockUserDataAccess;
    private MockGeminiAPIService mockGeminiAPIService;

    @BeforeEach
    void setUp() {
        mockOutputBoundary = new MockOutputBoundary();
        mockHealthDataAccess = new MockHealthDataAccess();
        mockUserDataAccess = new MockUserDataAccess();
        mockGeminiAPIService = new MockGeminiAPIService();

        interactor = new HealthInsightsInteractor(
                mockOutputBoundary,
                mockHealthDataAccess,
                mockUserDataAccess,
                mockGeminiAPIService
        );
    }

    @Test
    void testExecute_UserNotFound() {
        String userId = "non_existent_user";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);

        mockUserDataAccess.setUserToReturn(null);

        interactor.execute(inputData);

        assertTrue(mockOutputBoundary.isFailViewCalled());
        assertEquals("User not found", mockOutputBoundary.getErrorMessage());
        assertFalse(mockGeminiAPIService.wasCalled());
    }

    @Test
    void testExecute_NoHealthDataAvailable() {
        String userId = "user_with_no_data";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);
        User user = new User(userId, "password123", 25, 170, 70);

        mockUserDataAccess.setUserToReturn(user);
        mockHealthDataAccess.setHealthMetricsToReturn(Collections.emptyList());

        interactor.execute(inputData);

        assertTrue(mockOutputBoundary.isFailViewCalled());
        assertEquals("No health data available. Please log some metrics first.", mockOutputBoundary.getErrorMessage());
        assertFalse(mockGeminiAPIService.wasCalled());
    }

    @Test
    void testExecute_SuccessfulInsightsGeneration() {
        String userId = "valid_user";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);
        User user = new User(userId, "password123", 25, 170, 70);

        List<HealthMetrics> healthHistory = Arrays.asList(
                new HealthMetrics(userId, LocalDate.now().minusDays(1), 7.5, 8000, 2.0, 45, 2000),
                new HealthMetrics(userId, LocalDate.now(), 8.0, 10000, 2.5, 60, 2200)
        );

        mockUserDataAccess.setUserToReturn(user);
        mockHealthDataAccess.setHealthMetricsToReturn(healthHistory);
        mockGeminiAPIService.setInsightsToReturn("Great job! Your sleep has improved from 7.5 to 8.0 hours.");

        interactor.execute(inputData);

        assertTrue(mockOutputBoundary.isSuccessViewCalled());
        assertTrue(mockGeminiAPIService.wasCalled());
        assertEquals("Great job! Your sleep has improved from 7.5 to 8.0 hours.",
                mockOutputBoundary.getOutputData().getInsights());
    }

    @Test
    void testExecute_APIError() {
        String userId = "valid_user";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);
        User user = new User(userId, "password123", 25, 170, 70);

        List<HealthMetrics> healthHistory = Arrays.asList(
                new HealthMetrics(userId, LocalDate.now(), 7.5, 8000, 2.0, 45, 2000)
        );

        mockUserDataAccess.setUserToReturn(user);
        mockHealthDataAccess.setHealthMetricsToReturn(healthHistory);
        mockGeminiAPIService.setShouldThrowError(true);
        mockGeminiAPIService.setErrorMessage("API timeout");

        interactor.execute(inputData);

        assertTrue(mockOutputBoundary.isFailViewCalled());
        assertEquals("Error generating insights: API timeout", mockOutputBoundary.getErrorMessage());
        assertTrue(mockGeminiAPIService.wasCalled());
    }

    @Test
    void testExecute_GeneralException() {
        String userId = "valid_user";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);

        mockUserDataAccess.setShouldThrowException(true);
        mockUserDataAccess.setExceptionMessage("Database connection failed");

        interactor.execute(inputData);

        assertTrue(mockOutputBoundary.isFailViewCalled());
        assertEquals("Error generating insights: Database connection failed", mockOutputBoundary.getErrorMessage());
        assertFalse(mockGeminiAPIService.wasCalled());
    }

    @Test
    void testPrepareAnalysisData_WithMultipleMetrics() {
        String userId = "test_user";
        User user = new User(userId, "password123", 30, 180, 75);

        List<HealthMetrics> healthHistory = Arrays.asList(
                new HealthMetrics(userId, LocalDate.now().minusDays(2), 6.0, 5000, 1.5, 30, 1800),
                new HealthMetrics(userId, LocalDate.now().minusDays(1), 7.0, 7000, 2.0, 45, 2000),
                new HealthMetrics(userId, LocalDate.now(), 8.0, 9000, 2.5, 60, 2200)
        );

        mockUserDataAccess.setUserToReturn(user);
        mockHealthDataAccess.setHealthMetricsToReturn(healthHistory);
        mockGeminiAPIService.setInsightsToReturn("Test insights");

        interactor.execute(new HealthInsightsInputData(userId));

        assertTrue(mockGeminiAPIService.wasCalled());
        String analysisData = mockGeminiAPIService.getLastHealthData();
        assertNotNull(analysisData);
        assertTrue(analysisData.contains("User Profile: 30 years old, 180cm tall, 75kg."));
        assertTrue(analysisData.contains("Average Sleep: 7.0 hours"));
        assertTrue(analysisData.contains("Average Steps: 7000"));
    }

    private static class MockOutputBoundary implements HealthInsightsOutputBoundary {
        private boolean successViewCalled = false;
        private boolean failViewCalled = false;
        private HealthInsightsOutputData outputData;
        private String errorMessage;

        public boolean isSuccessViewCalled() { return successViewCalled; }
        public boolean isFailViewCalled() { return failViewCalled; }
        public HealthInsightsOutputData getOutputData() { return outputData; }
        public String getErrorMessage() { return errorMessage; }

        @Override
        public void prepareSuccessView(HealthInsightsOutputData outputData) {
            this.successViewCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failViewCalled = true;
            this.errorMessage = errorMessage;
        }
    }

    private static class MockHealthDataAccess implements HealthDataAccessInterface {
        private List<HealthMetrics> healthMetricsToReturn;

        public void setHealthMetricsToReturn(List<HealthMetrics> healthMetrics) {
            this.healthMetricsToReturn = healthMetrics;
        }

        @Override
        public List<HealthMetrics> getHealthMetricsByUser(String userId) {
            return healthMetricsToReturn;
        }

        @Override
        public void saveHealthMetrics(HealthMetrics healthMetrics) {
            // Not used in these tests
        }

        @Override
        public String getCurrentUsername() {
            return "test_user";
        }
    }

    private static class MockUserDataAccess implements UserDataAccessInterface {
        private User userToReturn;
        private boolean shouldThrowException = false;
        private String exceptionMessage = "";
        private String currentUsername = "test_user";

        public void setUserToReturn(User user) { this.userToReturn = user; }
        public void setShouldThrowException(boolean shouldThrow) { this.shouldThrowException = shouldThrow; }
        public void setExceptionMessage(String message) { this.exceptionMessage = message; }

        @Override
        public User get(String username) {
            if (shouldThrowException) {
                throw new RuntimeException(exceptionMessage);
            }
            return userToReturn;
        }

        @Override
        public boolean existsByName(String identifier) {
            return userToReturn != null && userToReturn.getName().equals(identifier);
        }

        @Override
        public void save(User user) {
            // Not used in these tests
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }

        @Override
        public void setCurrentUsername(String name) {
            this.currentUsername = name;
        }
    }

    private static class MockGeminiAPIService extends GeminiAPIService {
        private boolean wasCalled = false;
        private boolean shouldThrowError = false;
        private String insightsToReturn = "";
        private String errorMessage = "";
        private String lastHealthData = "";

        public void setInsightsToReturn(String insights) { this.insightsToReturn = insights; }
        public void setShouldThrowError(boolean shouldThrow) { this.shouldThrowError = shouldThrow; }
        public void setErrorMessage(String message) { this.errorMessage = message; }
        public boolean wasCalled() { return wasCalled; }
        public String getLastHealthData() { return lastHealthData; }

        @Override
        public void getHealthInsightsAsync(String healthData, InsightsCallback callback) {
            this.wasCalled = true;
            this.lastHealthData = healthData;

            if (shouldThrowError) {
                callback.onError(errorMessage);
            } else {
                callback.onSuccess(insightsToReturn);
            }
        }
    }
}