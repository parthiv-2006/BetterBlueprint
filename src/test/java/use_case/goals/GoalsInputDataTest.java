package use_case.goals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Goals Input Data verifying construction and getters.
 */
class GoalsInputDataTest {

    /**
     * Tests that constructor properly stores all provided values.
     */
    @Test
    void testConstructorWithAllValues() {
        GoalsInputData inputData = new GoalsInputData("Weight Loss", "85", "10");

        assertEquals("Weight Loss", inputData.getGoalType());
        assertEquals("85", inputData.getTarget());
        assertEquals("10", inputData.getTimeframe());
    }

    /**
     * Tests that constructor handles empty string parameters correctly.
     */
    @Test
    void testConstructorWithEmptyStrings() {
        GoalsInputData inputData = new GoalsInputData("Weight Maintenance", "", "");

        assertEquals("Weight Maintenance", inputData.getGoalType());
        assertEquals("", inputData.getTarget());
        assertEquals("", inputData.getTimeframe());
    }

    /**
     * Tests that constructor handles null parameters correctly.
     * Note: This only tests that GoalsInputData can store null values without error.
     * Actual validation and null checking is performed by GoalsInteractor.
     */
    @Test
    void testConstructorWithNullValues() {
        GoalsInputData inputData = new GoalsInputData(null, null, null);

        assertNull(inputData.getGoalType());
        assertNull(inputData.getTarget());
        assertNull(inputData.getTimeframe());
    }

    /**
     * Tests weight loss goal input data construction and access.
     */
    @Test
    void testWeightLossGoalInput() {
        GoalsInputData inputData = new GoalsInputData("Weight Loss", "75", "12");

        assertEquals("Weight Loss", inputData.getGoalType());
        assertEquals("75", inputData.getTarget());
        assertEquals("12", inputData.getTimeframe());
    }

    /**
     * Tests weight gain goal input data construction and access.
     */
    @Test
    void testWeightGainGoalInput() {
        GoalsInputData inputData = new GoalsInputData("Weight Gain", "95", "16");

        assertEquals("Weight Gain", inputData.getGoalType());
        assertEquals("95", inputData.getTarget());
        assertEquals("16", inputData.getTimeframe());
    }

    /**
     * Tests weight maintenance goal input data construction and access.
     */
    @Test
    void testWeightMaintenanceGoalInput() {
        GoalsInputData inputData = new GoalsInputData("Weight Maintenance", "", "1");

        assertEquals("Weight Maintenance", inputData.getGoalType());
        assertEquals("", inputData.getTarget());
        assertEquals("1", inputData.getTimeframe());
    }

    /**
     * Tests that target weight with decimal values is properly stored.
     * Note: This only tests that GoalsInputData correctly passes through decimal values.
     * Actual validation of whether decimal targets are acceptable is performed by GoalsInteractor.
     */
    @Test
    void testTargetWeightWithDecimal() {
        GoalsInputData inputData = new GoalsInputData("Weight Loss", "85.5", "10");

        assertEquals("Weight Loss", inputData.getGoalType());
        assertEquals("85.5", inputData.getTarget());
    }

    /**
     * Tests that target weight with negative sign is properly stored.
     * Note: This only tests that GoalsInputData correctly passes through negative values.
     * Actual validation that negative weights are invalid is performed by GoalsInteractor.
     */
    @Test
    void testTargetWeightWithNegativeSign() {
        GoalsInputData inputData = new GoalsInputData("Weight Loss", "-50", "10");

        assertEquals("-50", inputData.getTarget());
    }

    /**
     * Tests that timeframe with non-numeric characters is stored as-is.
     * Note: This only tests that GoalsInputData correctly passes through non-numeric values.
     * Actual validation that timeframe must be a valid integer is performed by GoalsInteractor.
     */
    @Test
    void testTimeframeWithNonNumeric() {
        GoalsInputData inputData = new GoalsInputData("Weight Loss", "85", "abc");

        assertEquals("abc", inputData.getTimeframe());
    }

    /**
     * Tests that getter methods return consistent values across multiple calls.
     */
    @Test
    void testAllGettersReturnConsistentValues() {
        String goalType = "Weight Loss";
        String target = "80";
        String timeframe = "12";

        GoalsInputData inputData = new GoalsInputData(goalType, target, timeframe);
        for (int i = 0; i < 3; i++) {
            assertEquals(goalType, inputData.getGoalType());
            assertEquals(target, inputData.getTarget());
            assertEquals(timeframe, inputData.getTimeframe());
        }
    }

    /**
     * Tests comprehensive coverage of all getters across multiple instances.
     */
    @Test
    void testComprehensiveCoverageAllGetters() {
        GoalsInputData a = new GoalsInputData("A", "1", "2");
        GoalsInputData b = new GoalsInputData("B", "", "3");
        GoalsInputData c = new GoalsInputData(null, "5.5", null);

        assertEquals("A", a.getGoalType());
        assertEquals("1", a.getTarget());
        assertEquals("2", a.getTimeframe());

        assertEquals("B", b.getGoalType());
        assertEquals("", b.getTarget());
        assertEquals("3", b.getTimeframe());

        assertNull(c.getGoalType());
        assertEquals("5.5", c.getTarget());
        assertNull(c.getTimeframe());
    }
}
