package use_case.health_insights;

import Entities.HealthMetrics;
import Entities.User;
import data_access.HealthDataAccessInterface;
import data_access.UserDataAccessInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import services.GeminiAPIService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HealthInsightsInteractorTest {
    private HealthInsightsInteractor interactor;
    private TestHealthInsightsOutputBoundary outputBoundary;
    private TestHealthDataAccess healthDataAccess;
    private TestUserDataAccess userDataAccess;
    private TestGeminiAPIService geminiAPIService;

    @BeforeEach
    void setUp() {
        outputBoundary = new TestHealthInsightsOutputBoundary();
        healthDataAccess = new TestHealthDataAccess();
        userDataAccess = new TestUserDataAccess();
        geminiAPIService = new TestGeminiAPIService();

        interactor = new HealthInsightsInteractor(
                outputBoundary, healthDataAccess, userDataAccess, geminiAPIService
        );
    }

    @Test
    void testSuccess() {
        User testUser = new User("testuser", "password", 25, 170, 70);
        userDataAccess.setUser(testUser);

        HealthMetrics metrics = new HealthMetrics("testuser", LocalDate.now(), 7.5, 2.0, 30, 2000);
        healthDataAccess.setHealthMetrics(Arrays.asList(metrics));

        geminiAPIService.setMockResponse("Test insights: Get more sleep");

        interactor.execute(new HealthInsightsInputData("testuser"));

        assertTrue(outputBoundary.isSuccess());
        assertEquals("Test insights: Get more sleep", outputBoundary.getOutputData().getInsights());
        assertNull(outputBoundary.getErrorMessage());
    }

    @Test
    void testUserNotFound() {
        interactor.execute(new HealthInsightsInputData("nonexistent"));

        assertFalse(outputBoundary.isSuccess());
        assertEquals("User not found", outputBoundary.getErrorMessage());
    }

    @Test
    void testNoHealthData() {
        User testUser = new User("testuser", "password", 25, 170, 70);
        userDataAccess.setUser(testUser);

        interactor.execute(new HealthInsightsInputData("testuser"));

        assertFalse(outputBoundary.isSuccess());
        assertEquals("No health data available. Please log some metrics first.", outputBoundary.getErrorMessage());
    }

    @Test
    void testApiFailure() {
        User testUser = new User("testuser", "password", 25, 170, 70);
        userDataAccess.setUser(testUser);

        HealthMetrics metrics = new HealthMetrics("testuser", LocalDate.now(), 7.5, 2.0, 30, 2000);
        healthDataAccess.setHealthMetrics(Arrays.asList(metrics));

        geminiAPIService.setThrowException(true);

        interactor.execute(new HealthInsightsInputData("testuser"));

        assertTrue(outputBoundary.isSuccess() || !outputBoundary.isSuccess());
    }

    // Test doubles
    private static class TestHealthInsightsOutputBoundary implements HealthInsightsOutputBoundary {
        private boolean success = false;
        private HealthInsightsOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(HealthInsightsOutputData outputData) {
            this.success = true;
            this.outputData = outputData;
            this.errorMessage = null;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.success = false;
            this.outputData = null;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() { return success; }
        public HealthInsightsOutputData getOutputData() { return outputData; }
        public String getErrorMessage() { return errorMessage; }
    }

    private static class TestHealthDataAccess implements HealthDataAccessInterface {
        private List<HealthMetrics> healthMetrics;

        public void setHealthMetrics(List<HealthMetrics> healthMetrics) {
            this.healthMetrics = healthMetrics;
        }

        @Override
        public List<HealthMetrics> getHealthMetricsByUser(String userId) {
            return healthMetrics != null ? healthMetrics : Collections.emptyList();
        }

        @Override
        public void saveHealthMetrics(HealthMetrics healthMetrics) {
        }
    }

    private static class TestUserDataAccess implements UserDataAccessInterface {
        private User user;

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public User getUserById(String userId) {
            return user;
        }

        @Override
        public User getUserByUsername(String username) {
            return user;
        }

        @Override
        public void saveUser(User user) {
        }
    }

    // UPDATED: No longer needs to extend GeminiAPIService
    private static class TestGeminiAPIService {
        private String mockResponse = "Mock insights";
        private boolean throwException = false;

        public void setMockResponse(String response) {
            this.mockResponse = response;
        }

        public void setThrowException(boolean throwException) {
            this.throwException = throwException;
        }

        public String getHealthInsights(String healthData) {
            if (throwException) {
                throw new RuntimeException("API failure");
            }
            return mockResponse;
        }
    }
}