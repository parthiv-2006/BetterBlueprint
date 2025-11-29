package use_case.health_insights;

import Entities.HealthMetrics;
import Entities.User;
import data_access.HealthDataAccessInterface;
import data_access.UserDataAccessInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.GeminiAPIService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthInsightsInteractorTest {

    @Mock
    private HealthInsightsOutputBoundary mockOutputBoundary;

    @Mock
    private HealthDataAccessInterface mockHealthDataAccess;

    @Mock
    private UserDataAccessInterface mockUserDataAccess;

    @Mock
    private GeminiAPIService mockGeminiService;

    private HealthInsightsInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new HealthInsightsInteractor(
                mockOutputBoundary,
                mockHealthDataAccess,
                mockUserDataAccess,
                mockGeminiService
        );
    }

    @Test
    void testExecute_UserNotFound() {
        // Arrange
        String userId = "non_existent_user";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);

        when(mockUserDataAccess.get(userId)).thenReturn(null);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockOutputBoundary).prepareFailView("User not found");
        verify(mockHealthDataAccess, never()).getHealthMetricsByUser(anyString());
        verify(mockGeminiService, never()).getHealthInsightsAsync(anyString(), any());
    }

    @Test
    void testExecute_NoHealthDataAvailable() {
        // Arrange
        String userId = "user_with_no_data";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);
        User user = new User(userId, "Test User", 25, 170, 70);

        when(mockUserDataAccess.get(userId)).thenReturn(user);
        when(mockHealthDataAccess.getHealthMetricsByUser(userId)).thenReturn(Collections.emptyList());

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockOutputBoundary).prepareFailView("No health data available. Please log some metrics first.");
        verify(mockGeminiService, never()).getHealthInsightsAsync(anyString(), any());
    }

    @Test
    void testExecute_SuccessfulInsightsGeneration() {
        // Arrange
        String userId = "valid_user";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);
        User user = new User(userId, "Test User", 25, 170, 70);

        List<HealthMetrics> healthHistory = Arrays.asList(
                new HealthMetrics(userId, 7.5, 8000, 2.0, 45, 2000),
                new HealthMetrics(userId, 8.0, 10000, 2.5, 60, 2200)
        );

        when(mockUserDataAccess.get(userId)).thenReturn(user);
        when(mockHealthDataAccess.getHealthMetricsByUser(userId)).thenReturn(healthHistory);

        // Act
        interactor.execute(inputData);

        // Assert - Verify API service is called with expected data
        verify(mockGeminiService).getHealthInsightsAsync(contains("User Profile: 25 years old, 170cm tall, 70kg."), any());
    }

    @Test
    void testExecute_APISuccessCallback() {
        // Arrange
        String userId = "valid_user";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);
        User user = new User(userId, "Test User", 25, 170, 70);

        List<HealthMetrics> healthHistory = Arrays.asList(
                new HealthMetrics(userId, 7.5, 8000, 2.0, 45, 2000)
        );

        when(mockUserDataAccess.get(userId)).thenReturn(user);
        when(mockHealthDataAccess.getHealthMetricsByUser(userId)).thenReturn(healthHistory);

        // Capture the callback
        doAnswer(invocation -> {
            GeminiAPIService.InsightsCallback callback = invocation.getArgument(1);
            callback.onSuccess("Generated health insights: Get more sleep!");
            return null;
        }).when(mockGeminiService).getHealthInsightsAsync(anyString(), any());

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockOutputBoundary).prepareSuccessView(argThat(outputData ->
                outputData.getInsights().equals("Generated health insights: Get more sleep!")
        ));
    }

    @Test
    void testExecute_APIErrorCallback() {
        // Arrange
        String userId = "valid_user";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);
        User user = new User(userId, "Test User", 25, 170, 70);

        List<HealthMetrics> healthHistory = Arrays.asList(
                new HealthMetrics(userId, 7.5, 8000, 2.0, 45, 2000)
        );

        when(mockUserDataAccess.get(userId)).thenReturn(user);
        when(mockHealthDataAccess.getHealthMetricsByUser(userId)).thenReturn(healthHistory);

        // Capture the callback with error
        doAnswer(invocation -> {
            GeminiAPIService.InsightsCallback callback = invocation.getArgument(1);
            callback.onError("API timeout");
            return null;
        }).when(mockGeminiService).getHealthInsightsAsync(anyString(), any());

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockOutputBoundary).prepareFailView("Error generating insights: API timeout");
    }

    @Test
    void testExecute_ExceptionHandling() {
        // Arrange
        String userId = "valid_user";
        HealthInsightsInputData inputData = new HealthInsightsInputData(userId);

        when(mockUserDataAccess.get(userId)).thenThrow(new RuntimeException("Database error"));

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockOutputBoundary).prepareFailView("Error generating insights: Database error");
    }

    @Test
    void testPrepareAnalysisData_WithTrends() {
        // Arrange
        String userId = "test_user";
        User user = new User(userId, "Test User", 30, 180, 75);

        List<HealthMetrics> healthHistory = Arrays.asList(
                new HealthMetrics(userId, 6.0, 5000, 1.5, 30, 1800),
                new HealthMetrics(userId, 7.0, 7000, 2.0, 45, 2000),
                new HealthMetrics(userId, 8.0, 9000, 2.5, 60, 2200)
        );

        // Act - Using reflection to test private method, or you can make it package-private for testing
        String result = interactor.executeForTest(user, healthHistory);

        // Assert
        assert(result.contains("User Profile: 30 years old, 180cm tall, 75kg."));
        assert(result.contains("Average Sleep: 7.0 hours"));
        assert(result.contains("Average Steps: 7000"));
    }

    // Helper method to test private method - add this to your interactor temporarily for testing
    // public String executeForTest(User user, List<HealthMetrics> healthHistory) {
    //     return prepareAnalysisData(user, healthHistory);
    // }
}