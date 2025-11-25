package use_case.healthHistory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.healthHistory.healthHistoryInteractor;
import use_case.healthHistory.healthHistoryOutputBoundary;
import use_case.healthHistory.healthHistoryOutputData;
import use_case.healthHistory.healthMetricRecord;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for healthHistoryInteractor using the file-based JSON fallback.
 */
public class HealthHistoryInteractorTest {

    private final Path jsonPath = Path.of("health_metrics.json");

    @BeforeEach
    void setUp() throws Exception {
        // create sample JSON with three records for user1 at different ages
        LocalDate now = LocalDate.now();
        String r1 = String.format("{\"user\":\"user1\",\"date\":\"%s\",\"calories\":200,\"steps\":3000,\"sleepHours\":7.0,\"waterIntake\":1500,\"exerciseMinutes\":30}",
                now.minusDays(3).toString());   // within week
        String r2 = String.format("{\"user\":\"user1\",\"date\":\"%s\",\"calories\":150,\"steps\":2500,\"sleepHours\":6.5,\"waterIntake\":1200,\"exerciseMinutes\":20}",
                now.minusDays(10).toString());  // within month, outside week
        String r3 = String.format("{\"user\":\"user1\",\"date\":\"%s\",\"calories\":100,\"steps\":2000,\"sleepHours\":6.0,\"waterIntake\":1000,\"exerciseMinutes\":15}",
                now.minusDays(400).toString()); // outside year

        String json = "[" + r1 + "," + r2 + "," + r3 + "]";
        Files.writeString(jsonPath, json);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(jsonPath);
    }

    @Test
    void testFetchCalories_weekFiltersCorrectly() {
        MockPresenter presenter = new MockPresenter();
        healthHistoryInteractor interactor = new healthHistoryInteractor(null, presenter);

        interactor.fetchHistory("calories", "week", "user1");

        assertTrue(presenter.successCalled, "Presenter should be called on success");
        healthHistoryOutputData out = presenter.outputData;
        assertNotNull(out);
        List<healthMetricRecord> records = out.getRecords();
        assertEquals("calories", out.getMetricType());
        assertEquals(1, records.size());
        assertEquals(200.0, records.get(0).getValue(), 0.001);
    }

    @Test
    void testFetchCalories_monthFiltersCorrectly() {
        MockPresenter presenter = new MockPresenter();
        healthHistoryInteractor interactor = new healthHistoryInteractor(null, presenter);

        interactor.fetchHistory("calories", "month", "user1");

        assertTrue(presenter.successCalled);
        healthHistoryOutputData out = presenter.outputData;
        assertNotNull(out);
        List<healthMetricRecord> records = out.getRecords();
        assertEquals(2, records.size()); // r1 and r2
    }

    @Test
    void testFetchCalories_yearReturnsAllWithinYear() {
        MockPresenter presenter = new MockPresenter();
        healthHistoryInteractor interactor = new healthHistoryInteractor(null, presenter);

        interactor.fetchHistory("calories", "year", "user1");

        assertTrue(presenter.successCalled);
        healthHistoryOutputData out = presenter.outputData;
        assertNotNull(out);
        List<healthMetricRecord> records = out.getRecords();
        assertEquals(2, records.size());
    }

    @Test
    void testFetchForUnknownUserReturnsEmpty() {
        MockPresenter presenter = new MockPresenter();
        healthHistoryInteractor interactor = new healthHistoryInteractor(null, presenter);

        interactor.fetchHistory("calories", "year", "nonexistent_user");

        assertTrue(presenter.successCalled);
        healthHistoryOutputData out = presenter.outputData;
        assertNotNull(out);
        assertTrue(out.getRecords().isEmpty());
    }

    private static class MockPresenter implements healthHistoryOutputBoundary {
        boolean successCalled = false;
        healthHistoryOutputData outputData;

        @Override
        public void prepareSuccessView(healthHistoryOutputData outputData) {
            this.successCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            // unused in these tests
        }
    }
}

