package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeminiAPIServiceTest {

    @Test
    void testMockService() {
        MockGeminiAPIService mockService = new MockGeminiAPIService();
        String insights = mockService.getHealthInsights("test data");
        assertNotNull(insights);
        assertFalse(insights.isEmpty());
        assertEquals("Mock insights for: test data", insights);
    }

    @Test
    void testMockServiceWithEmptyData() {
        MockGeminiAPIService mockService = new MockGeminiAPIService();
        String insights = mockService.getHealthInsights("");
        assertNotNull(insights);
        assertEquals("Mock insights for: ", insights);
    }

    @Test
    void testMockServiceWithNullData() {
        MockGeminiAPIService mockService = new MockGeminiAPIService();
        String insights = mockService.getHealthInsights(null);
        assertNotNull(insights);
        assertEquals("Mock insights for: null", insights);
    }

    @Test
    void testAsyncCallbackSuccess() {
        MockGeminiAPIService mockService = new MockGeminiAPIService();

        TestInsightsCallback callback = new TestInsightsCallback();
        mockService.getHealthInsightsAsync("test data", callback);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(callback.wasSuccessCalled());
        assertEquals("Mock insights for: test data", callback.getSuccessResult());
        assertFalse(callback.wasErrorCalled());
    }

    @Test
    void testAsyncCallbackError() {
        MockGeminiAPIService mockService = new MockGeminiAPIService();
        mockService.setShouldThrowException(true);

        TestInsightsCallback callback = new TestInsightsCallback();
        mockService.getHealthInsightsAsync("test data", callback);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(callback.wasErrorCalled());
        assertFalse(callback.wasSuccessCalled());
        assertEquals("Test exception", callback.getErrorResult());
    }

    @Test
    void testRealServiceInitializationWithoutApiKey() {
        try {
            GeminiAPIService service = new GeminiAPIService();
            assertNotNull(service);
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("GEMINI_API_KEY"));
        }
    }

    private static class TestInsightsCallback implements GeminiAPIService.InsightsCallback {
        private boolean successCalled = false;
        private boolean errorCalled = false;
        private String successResult;
        private String errorResult;

        @Override
        public void onSuccess(String insights) {
            successCalled = true;
            successResult = insights;
        }

        @Override
        public void onError(String errorMessage) {
            errorCalled = true;
            errorResult = errorMessage;
        }

        public boolean wasSuccessCalled() {
            return successCalled;
        }

        public boolean wasErrorCalled() {
            return errorCalled;
        }

        public String getSuccessResult() {
            return successResult;
        }

        public String getErrorResult() {
            return errorResult;
        }
    }
}

class MockGeminiAPIService extends GeminiAPIService {
    private boolean shouldThrowException = false;

    public void setShouldThrowException(boolean shouldThrow) {
        this.shouldThrowException = shouldThrow;
    }

    @Override
    public void getHealthInsightsAsync(String healthData, InsightsCallback callback) {
        if (shouldThrowException) {
            callback.onError("Test exception");
        } else {
            callback.onSuccess(getHealthInsights(healthData));
        }
    }

    public String getHealthInsights(String healthData) {
        return "Mock insights for: " + healthData;
    }
}