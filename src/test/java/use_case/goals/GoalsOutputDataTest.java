package use_case.goals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GoalsOutputDataTest {

    @Test
    public void testFullConstructorWithAllParameters() {
        GoalsOutputData outputData = new GoalsOutputData(
                "Weight Loss",
                "1800",
                "2400",
                "To lose weight, maintain a caloric deficit.",
                100,
                false,
                "",
                "85",
                "10"
        );
        assertEquals("Weight Loss", outputData.getGoalType());
        assertEquals("1800", outputData.getDailyIntakeCalories());
        assertEquals("2400", outputData.getDailyBurnCalories());
        assertEquals("To lose weight, maintain a caloric deficit.", outputData.getExplanation());
        assertEquals(100, outputData.getCurrentWeightKg());
        assertFalse(outputData.shouldRedirectToSettings());
        assertEquals("85", outputData.getTarget());
        assertEquals("10", outputData.getTimeframe());
    }

    @Test
    public void testShortConstructorWithSevenParameters() {
        GoalsOutputData outputData = new GoalsOutputData(
                "Weight Gain",
                "2500",
                "2200",
                "To gain weight, maintain a caloric surplus.",
                80,
                false,
                ""
        );

        assertEquals("Weight Gain", outputData.getGoalType());
        assertEquals("2500", outputData.getDailyIntakeCalories());
        assertEquals("2200", outputData.getDailyBurnCalories());
        assertEquals("", outputData.getTarget());
        assertEquals("", outputData.getTimeframe());
    }

    @Test
    public void testWeightLossGoalOutput() {
        GoalsOutputData outputData = new GoalsOutputData(
                "Weight Loss",
                "1700",
                "2300",
                "To lose weight, maintain a caloric deficit by consuming less than your daily burn.",
                95,
                false,
                "",
                "80",
                "12"
        );

        assertEquals("Weight Loss", outputData.getGoalType());
        assertTrue(Double.parseDouble(outputData.getDailyIntakeCalories()) <
                   Double.parseDouble(outputData.getDailyBurnCalories()));
        assertEquals(95, outputData.getCurrentWeightKg());
    }

    @Test
    public void testWeightGainGoalOutput() {
        GoalsOutputData outputData = new GoalsOutputData(
                "Weight Gain",
                "2600",
                "2200",
                "To gain weight, maintain a caloric surplus by consuming more than your daily burn.",
                75,
                false,
                "",
                "95",
                "15"
        );

        assertEquals("Weight Gain", outputData.getGoalType());
        assertTrue(Double.parseDouble(outputData.getDailyIntakeCalories()) >
                   Double.parseDouble(outputData.getDailyBurnCalories()));
        assertEquals(75, outputData.getCurrentWeightKg());
    }

    @Test
    public void testWeightMaintenanceGoalOutput() {
        GoalsOutputData outputData = new GoalsOutputData(
                "Weight Maintenance",
                "2300",
                "2300",
                "Maintain your current weight by consuming approximately your daily burn calories.",
                100,
                false,
                ""
        );

        assertEquals("Weight Maintenance", outputData.getGoalType());
        assertEquals("2300", outputData.getDailyIntakeCalories());
        assertEquals("2300", outputData.getDailyBurnCalories());
    }

    @Test
    public void testRedirectToSettingsTrue() {
        GoalsOutputData outputData = new GoalsOutputData(
                "Weight Loss",
                "1800",
                "2400",
                "Explanation",
                0,
                true,
                "Please input your weight in Settings before using Goals."
        );

        assertTrue(outputData.shouldRedirectToSettings());
        assertEquals("Please input your weight in Settings before using Goals.",
                     outputData.getRedirectMessage());
        assertEquals(0, outputData.getCurrentWeightKg());
    }

    @Test
    public void testCurrentWeightVariations() {
        GoalsOutputData output1 = new GoalsOutputData("Weight Loss", "1800", "2400", "Exp", 95, false, "", "85", "10");
        GoalsOutputData output2 = new GoalsOutputData("Weight Loss", "1800", "2400", "Exp", 0, true, "Weight not set");
        GoalsOutputData output3 = new GoalsOutputData("Weight Gain", "2500", "2200", "Exp", 200, false, "", "150", "20");

        assertEquals(95, output1.getCurrentWeightKg());
        assertEquals(0, output2.getCurrentWeightKg());
        assertEquals(200, output3.getCurrentWeightKg());
    }

    @Test
    public void testCalorieValuesAsRoundedIntegers() {
        GoalsOutputData outputData = new GoalsOutputData(
                "Weight Loss", "1800", "2400", "Exp", 100, false, "", "85", "10"
        );

        String intake = outputData.getDailyIntakeCalories();
        String burn = outputData.getDailyBurnCalories();

        assertTrue(intake.matches("\\d+"));
        assertTrue(burn.matches("\\d+"));
    }

    @Test
    public void testCalorieValuesAsDecimalNumbers() {
        GoalsOutputData outputData = new GoalsOutputData(
                "Weight Loss", "1850.5", "2425.3", "Exp", 100, false, "", "85", "10"
        );

        assertEquals("1850.5", outputData.getDailyIntakeCalories());
        assertEquals("2425.3", outputData.getDailyBurnCalories());
    }

    @Test
    public void testMultipleOutputDataInstances() {
        GoalsOutputData output1 = new GoalsOutputData(
                "Weight Loss", "1800", "2400", "Loss explanation", 100, false, "", "85", "10"
        );
        GoalsOutputData output2 = new GoalsOutputData(
                "Weight Gain", "2600", "2200", "Gain explanation", 75, false, "", "95", "15"
        );

        assertEquals("Weight Loss", output1.getGoalType());
        assertEquals("Weight Gain", output2.getGoalType());
        assertEquals("1800", output1.getDailyIntakeCalories());
        assertEquals("2600", output2.getDailyIntakeCalories());
    }
}

