package use_case.healthHistory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for healthHistoryInputData.
 */
public class HealthHistoryInputDataTest {

    @Test
    void testGettersReturnConstructorValues() {
        healthHistoryInputData input = new healthHistoryInputData("calories", "week", "user1");

        assertEquals("calories", input.getMetricType());
        assertEquals("week", input.getTimeRange());
        assertEquals("user1", input.getUser());
    }

    @Test
    void testDifferentValues() {
        healthHistoryInputData input = new healthHistoryInputData("waterIntake", "month", "user2");

        assertEquals("waterIntake", input.getMetricType());
        assertEquals("month", input.getTimeRange());
        assertEquals("user2", input.getUser());
    }
}