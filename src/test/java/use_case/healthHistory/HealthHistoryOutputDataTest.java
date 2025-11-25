package use_case.healthHistory;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for healthHistoryOutputData.
 */
public class HealthHistoryOutputDataTest {

    @Test
    void testOutputDataHoldsValuesAndRecords() {
        healthMetricRecord r = new healthMetricRecord(LocalDate.of(2023, 1, 1), 123.45);
        List<healthMetricRecord> records = Arrays.asList(r);

        healthHistoryOutputData out = new healthHistoryOutputData("month", "calories", records);

        assertEquals("month", out.getTimeRange());
        assertEquals("calories", out.getMetricType());
        assertNotNull(out.getRecords());
        assertEquals(1, out.getRecords().size());
        assertEquals(123.45, out.getRecords().get(0).getValue(), 0.0001);
        assertEquals(LocalDate.of(2023, 1, 1), out.getRecords().get(0).getDate());
    }
}
