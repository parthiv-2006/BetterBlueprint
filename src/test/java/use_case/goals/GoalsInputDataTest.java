package use_case.goals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GoalsInputDataTest {

    @Test
    public void testConstructorWithAllValues() {
        GoalsInputData inputData = new GoalsInputData("Weight Loss", "85", "10");

        assertEquals("Weight Loss", inputData.getGoalType());
        assertEquals("85", inputData.getTarget());
        assertEquals("10", inputData.getTimeframe());
    }

    @Test
    public void testConstructorWithEmptyStrings() {
        GoalsInputData inputData = new GoalsInputData("Weight Maintenance", "", "");

        assertEquals("Weight Maintenance", inputData.getGoalType());
        assertEquals("", inputData.getTarget());
        assertEquals("", inputData.getTimeframe());
    }

    @Test
    public void testConstructorWithNullValues() {
        GoalsInputData inputData = new GoalsInputData(null, null, null);

        assertNull(inputData.getGoalType());
        assertNull(inputData.getTarget());
        assertNull(inputData.getTimeframe());
    }

    @Test
    public void testWeightLossGoalInput() {
        GoalsInputData inputData = new GoalsInputData("Weight Loss", "75", "12");

        assertEquals("Weight Loss", inputData.getGoalType());
        assertEquals("75", inputData.getTarget());
        assertEquals("12", inputData.getTimeframe());
    }

    @Test
    public void testWeightGainGoalInput() {
        GoalsInputData inputData = new GoalsInputData("Weight Gain", "95", "16");

        assertEquals("Weight Gain", inputData.getGoalType());
        assertEquals("95", inputData.getTarget());
        assertEquals("16", inputData.getTimeframe());
    }

    @Test
    public void testWeightMaintenanceGoalInput() {
        GoalsInputData inputData = new GoalsInputData("Weight Maintenance", "", "1");

        assertEquals("Weight Maintenance", inputData.getGoalType());
        assertEquals("", inputData.getTarget());
        assertEquals("1", inputData.getTimeframe());
    }

    @Test
    public void testTargetWeightWithDecimal() {
        GoalsInputData inputData = new GoalsInputData("Weight Loss", "85.5", "10");

        assertEquals("Weight Loss", inputData.getGoalType());
        assertEquals("85.5", inputData.getTarget());
    }

    @Test
    public void testTargetWeightWithNegativeSign() {
        GoalsInputData inputData = new GoalsInputData("Weight Loss", "-50", "10");

        assertEquals("-50", inputData.getTarget());
    }

    @Test
    public void testTimeframeWithNonNumeric() {
        GoalsInputData inputData = new GoalsInputData("Weight Loss", "85", "abc");

        assertEquals("abc", inputData.getTimeframe());
    }

    @Test
    public void testAllGettersReturnConsistentValues() {
        String goalType = "Weight Loss";
        String target = "80";
        String timeframe = "12";

        GoalsInputData inputData = new GoalsInputData(goalType, target, timeframe);

        // Call getters multiple times and verify consistency
        for (int i = 0; i < 3; i++) {
            assertEquals(goalType, inputData.getGoalType());
            assertEquals(target, inputData.getTarget());
            assertEquals(timeframe, inputData.getTimeframe());
        }
    }
}



