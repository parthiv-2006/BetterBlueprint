package use_case.health_insights;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HealthInsightsInputDataTest {

    @Test
    void testGetUserId() {
        HealthInsightsInputData inputData = new HealthInsightsInputData("test_user_123");
        assertEquals("test_user_123", inputData.getUserId());
    }

    @Test
    void testDifferentUserId() {
        HealthInsightsInputData inputData = new HealthInsightsInputData("different_user");
        assertEquals("different_user", inputData.getUserId());
    }
}