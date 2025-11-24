package use_case.health_insights;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HealthInsightsOutputDataTest {

    @Test
    void testGetInsights() {
        String testInsights = "You should aim for 8 hours of sleep and drink 2L of water daily.";
        HealthInsightsOutputData outputData = new HealthInsightsOutputData(testInsights);

        assertEquals(testInsights, outputData.getInsights());
    }

    @Test
    void testEmptyInsights() {
        HealthInsightsOutputData outputData = new HealthInsightsOutputData("");
        assertEquals("", outputData.getInsights());
    }

    @Test
    void testNullInsights() {
        HealthInsightsOutputData outputData = new HealthInsightsOutputData(null);
        assertNull(outputData.getInsights());
    }
}