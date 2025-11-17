package java.test.entity;

import Entities.HealthMetrics;
import java.time.LocalDate;

public class HealthMetricsTest {
    private HealthMetrics validHealthMetrics;
    private final String testUserId = "user123";
    private final LocalDate testDate = LocalDate.of(2024, 12, 1);

    public void setUp() {
        validHealthMetrics = new HealthMetrics(
                testUserId,
                testDate,
                7.5,      // sleepHours
                2.0,      // waterIntake
                30.0,     // exerciseMinutes
                2000      // calories
        );
    }

    // Test constructor and getters
    public void testConstructorAndGetters() {
        setUp();
        assert testUserId.equals(validHealthMetrics.getUserId()) : "UserId should match";
        assert testDate.equals(validHealthMetrics.getDate()) : "Date should match";
        assert validHealthMetrics.getSleepHours() == 7.5 : "Sleep hours should be 7.5";
        assert validHealthMetrics.getWaterIntake() == 2.0 : "Water intake should be 2.0";
        assert validHealthMetrics.getExerciseMinutes() == 30.0 : "Exercise minutes should be 30.0";
        assert validHealthMetrics.getCalories() == 2000 : "Calories should be 2000";
    }

    // Test setters with valid values
    public void testSettersWithValidValues() {
        setUp();
        validHealthMetrics.setSleepHours(8.5);
        validHealthMetrics.setWaterIntake(3.0);
        validHealthMetrics.setExerciseMinutes(45.0);
        validHealthMetrics.setCalories(1800);

        assert validHealthMetrics.getSleepHours() == 8.5 : "Sleep hours should be updated to 8.5";
        assert validHealthMetrics.getWaterIntake() == 3.0 : "Water intake should be updated to 3.0";
        assert validHealthMetrics.getExerciseMinutes() == 45.0 : "Exercise minutes should be updated to 45.0";
        assert validHealthMetrics.getCalories() == 1800 : "Calories should be updated to 1800";
    }

    // Test validation with valid data
    public void testValidateMetricsWithValidData() {
        setUp();
        assert validHealthMetrics.validateMetrics() : "Valid metrics should pass validation";
    }

    // Test getSummary method
    public void testGetSummary() {
        setUp();
        String expectedSummary = "Health Summary for 2024-12-01: Sleep: 7.5 hours, Water: 2.0L, Exercise: 30.0 minutes, Calories: 2000";
        String actualSummary = validHealthMetrics.getSummary();
        assert expectedSummary.equals(actualSummary) : "Summary should match expected format";
    }

    // Main method to run all tests
    public static void main(String[] args) {
        HealthMetricsTest test = new HealthMetricsTest();

        System.out.println("Running HealthMetrics tests...");

        test.testConstructorAndGetters();
        System.out.println("TestConstructorAndGetters passed");

        test.testSettersWithValidValues();
        System.out.println("TestSettersWithValidValues passed");

        test.testValidateMetricsWithValidData();
        System.out.println("TestValidateMetricsWithValidData passed");

        test.testGetSummary();
        System.out.println("TestGetSummary passed");

        System.out.println("All tests passed!");
    }
}
