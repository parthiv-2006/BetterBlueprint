package use_case.daily_health_score;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DailyHealthScoreInputData.
 */
class DailyHealthScoreInputDataTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 29);
        String userId = "testUser123";

        // Act
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(testDate, userId);

        // Assert
        assertNotNull(inputData, "InputData should not be null");
        assertEquals(testDate, inputData.getDate(), "Date should match");
        assertEquals(userId, inputData.getUserId(), "User ID should match");
    }

    @Test
    void testWithCurrentDate() {
        // Arrange
        LocalDate today = LocalDate.now();
        String userId = "currentUser";

        // Act
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(today, userId);

        // Assert
        assertEquals(today, inputData.getDate(), "Should accept current date");
        assertEquals(userId, inputData.getUserId(), "User ID should match");
    }

    @Test
    void testWithPastDate() {
        // Arrange
        LocalDate pastDate = LocalDate.of(2024, 1, 1);
        String userId = "historyUser";

        // Act
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(pastDate, userId);

        // Assert
        assertEquals(pastDate, inputData.getDate(), "Should accept past date");
        assertEquals(userId, inputData.getUserId(), "User ID should match");
    }

    @Test
    void testWithFutureDate() {
        // Arrange
        LocalDate futureDate = LocalDate.of(2026, 12, 31);
        String userId = "futureUser";

        // Act
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(futureDate, userId);

        // Assert
        assertEquals(futureDate, inputData.getDate(), "Should accept future date");
        assertEquals(userId, inputData.getUserId(), "User ID should match");
    }

    @Test
    void testWithEmptyUserId() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 29);
        String emptyUserId = "";

        // Act
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(testDate, emptyUserId);

        // Assert
        assertEquals(emptyUserId, inputData.getUserId(), "Should accept empty user ID");
        assertEquals(testDate, inputData.getDate(), "Date should still match");
    }

    @Test
    void testWithSpecialCharactersInUserId() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 29);
        String specialUserId = "user@email.com";

        // Act
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(testDate, specialUserId);

        // Assert
        assertEquals(specialUserId, inputData.getUserId(), "Should accept special characters in user ID");
        assertEquals(testDate, inputData.getDate(), "Date should match");
    }

    @Test
    void testImmutability() {
        // Arrange
        LocalDate originalDate = LocalDate.of(2025, 11, 29);
        String originalUserId = "testUser";
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(originalDate, originalUserId);

        // Act
        LocalDate retrievedDate = inputData.getDate();
        String retrievedUserId = inputData.getUserId();

        // Assert
        assertEquals(originalDate, retrievedDate, "Retrieved date should match original");
        assertEquals(originalUserId, retrievedUserId, "Retrieved user ID should match original");

        // Verify that modifying the retrieved date doesn't affect the input data
        LocalDate modifiedDate = retrievedDate.plusDays(1);
        assertNotEquals(modifiedDate, inputData.getDate(), "Input data should be immutable");
    }

    @Test
    void testEquality() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 29);
        String userId = "testUser";

        // Act
        DailyHealthScoreInputData inputData1 = new DailyHealthScoreInputData(testDate, userId);
        DailyHealthScoreInputData inputData2 = new DailyHealthScoreInputData(testDate, userId);

        // Assert
        assertEquals(inputData1.getDate(), inputData2.getDate(), "Dates should be equal");
        assertEquals(inputData1.getUserId(), inputData2.getUserId(), "User IDs should be equal");
    }

    @Test
    void testDifferentDates() {
        // Arrange
        LocalDate date1 = LocalDate.of(2025, 11, 29);
        LocalDate date2 = LocalDate.of(2025, 11, 30);
        String userId = "testUser";

        // Act
        DailyHealthScoreInputData inputData1 = new DailyHealthScoreInputData(date1, userId);
        DailyHealthScoreInputData inputData2 = new DailyHealthScoreInputData(date2, userId);

        // Assert
        assertNotEquals(inputData1.getDate(), inputData2.getDate(), "Different dates should not be equal");
        assertEquals(inputData1.getUserId(), inputData2.getUserId(), "User IDs should still be equal");
    }

    @Test
    void testDifferentUserIds() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 29);
        String userId1 = "user1";
        String userId2 = "user2";

        // Act
        DailyHealthScoreInputData inputData1 = new DailyHealthScoreInputData(testDate, userId1);
        DailyHealthScoreInputData inputData2 = new DailyHealthScoreInputData(testDate, userId2);

        // Assert
        assertEquals(inputData1.getDate(), inputData2.getDate(), "Dates should be equal");
        assertNotEquals(inputData1.getUserId(), inputData2.getUserId(), "Different user IDs should not be equal");
    }

    @Test
    void testLeapYearDate() {
        // Arrange
        LocalDate leapYearDate = LocalDate.of(2024, 2, 29);
        String userId = "leapUser";

        // Act
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(leapYearDate, userId);

        // Assert
        assertEquals(leapYearDate, inputData.getDate(), "Should handle leap year dates");
        assertEquals(29, inputData.getDate().getDayOfMonth(), "Should be the 29th day");
    }

    @Test
    void testWithNullDate() {
        // Arrange
        LocalDate nullDate = null;
        String userId = "testUser";

        // Act & Assert
        assertDoesNotThrow(() -> {
            DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(nullDate, userId);
            assertNull(inputData.getDate(), "Should allow null date if not validated");
        });
    }

    @Test
    void testWithNullUserId() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 11, 29);
        String nullUserId = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(testDate, nullUserId);
            assertNull(inputData.getUserId(), "Should allow null userId if not validated");
        });
    }

    @Test
    void testMultipleInstancesIndependence() {
        // Arrange & Act
        DailyHealthScoreInputData input1 = new DailyHealthScoreInputData(
                LocalDate.of(2025, 11, 29), "user1"
        );
        DailyHealthScoreInputData input2 = new DailyHealthScoreInputData(
                LocalDate.of(2025, 11, 30), "user2"
        );

        // Assert
        assertNotEquals(input1.getDate(), input2.getDate(), "Dates should be different");
        assertNotEquals(input1.getUserId(), input2.getUserId(), "User IDs should be different");
    }

    @Test
    void testRealisticUsageScenario() {
        // Arrange - Simulate a realistic scenario where a user wants to check their score for yesterday
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String currentUser = "john.doe@example.com";

        // Act
        DailyHealthScoreInputData inputData = new DailyHealthScoreInputData(yesterday, currentUser);

        // Assert
        assertEquals(yesterday, inputData.getDate(), "Should correctly store yesterday's date");
        assertEquals(currentUser, inputData.getUserId(), "Should correctly store user ID");
        assertNotNull(inputData, "InputData should be successfully created");
    }
}

