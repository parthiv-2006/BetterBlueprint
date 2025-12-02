package use_case.settings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SettingsInputData.
 */
public class SettingsInputDataTest {

    /**
     * Tests the constructor and getters with valid numeric string values for age, height, and weight.
     */
    @Test
    public void testConstructorAndGettersWithValidValues() {
        // Arrange
        String age = "25";
        String height = "175";
        String weight = "70";

        // Act
        SettingsInputData inputData = new SettingsInputData(age, height, weight);

        // Assert
        assertNotNull(inputData, "InputData should not be null");
        assertEquals(age, inputData.getAge(), "Age should match");
        assertEquals(height, inputData.getHeight(), "Height should match");
        assertEquals(weight, inputData.getWeight(), "Weight should match");
    }

    /**
     * Tests the constructor with empty string values for all fields.
     */
    @Test
    public void testConstructorWithEmptyStrings() {
        // Arrange & Act
        SettingsInputData inputData = new SettingsInputData("", "", "");

        // Assert
        assertEquals("", inputData.getAge(), "Age should be empty string");
        assertEquals("", inputData.getHeight(), "Height should be empty string");
        assertEquals("", inputData.getWeight(), "Weight should be empty string");
    }

    /**
     * Tests the constructor with null values for all fields.
     */
    @Test
    public void testConstructorWithNullValues() {
        // Arrange & Act
        SettingsInputData inputData = new SettingsInputData(null, null, null);

        // Assert
        assertNull(inputData.getAge(), "Age should be null");
        assertNull(inputData.getHeight(), "Height should be null");
        assertNull(inputData.getWeight(), "Weight should be null");
    }

    /**
     * Tests the constructor with typical adult age range values.
     */
    @Test
    public void testWithTypicalAdultValues() {
        // Arrange & Act
        SettingsInputData inputData = new SettingsInputData("30", "180", "75");

        // Assert
        assertEquals("30", inputData.getAge());
        assertEquals("180", inputData.getHeight());
        assertEquals("75", inputData.getWeight());
    }

    /**
     * Tests the constructor with minimum realistic values.
     */
    @Test
    public void testWithMinimumRealisticValues() {
        // Arrange & Act
        SettingsInputData inputData = new SettingsInputData("18", "150", "45");

        // Assert
        assertEquals("18", inputData.getAge());
        assertEquals("150", inputData.getHeight());
        assertEquals("45", inputData.getWeight());
    }

    /**
     * Tests the constructor with high values (elderly person, tall height, heavy weight).
     */
    @Test
    public void testWithHighValues() {
        // Arrange & Act
        SettingsInputData inputData = new SettingsInputData("85", "200", "120");

        // Assert
        assertEquals("85", inputData.getAge());
        assertEquals("200", inputData.getHeight());
        assertEquals("120", inputData.getWeight());
    }

    /**
     * Tests that getters return consistent values when called multiple times.
     */
    @Test
    public void testGettersReturnConsistentValues() {
        // Arrange
        String age = "28";
        String height = "170";
        String weight = "68";
        SettingsInputData inputData = new SettingsInputData(age, height, weight);

        // Act & Assert - Call getters multiple times to ensure consistency
        for (int i = 0; i < 3; i++) {
            assertEquals(age, inputData.getAge(), "Age should remain consistent");
            assertEquals(height, inputData.getHeight(), "Height should remain consistent");
            assertEquals(weight, inputData.getWeight(), "Weight should remain consistent");
        }
    }

    /**
     * Tests the constructor with decimal values (even though they're stored as strings).
     */
    @Test
    public void testWithDecimalValues() {
        // Arrange & Act
        SettingsInputData inputData = new SettingsInputData("25.5", "175.5", "70.5");

        // Assert
        assertEquals("25.5", inputData.getAge());
        assertEquals("175.5", inputData.getHeight());
        assertEquals("70.5", inputData.getWeight());
    }

    /**
     * Tests the constructor with negative values (edge case that should be handled by validation).
     */
    @Test
    public void testWithNegativeValues() {
        // Arrange & Act
        SettingsInputData inputData = new SettingsInputData("-5", "-180", "-70");

        // Assert
        assertEquals("-5", inputData.getAge());
        assertEquals("-180", inputData.getHeight());
        assertEquals("-70", inputData.getWeight());
    }

    /**
     * Tests the constructor with non-numeric string values (edge case).
     */
    @Test
    public void testWithNonNumericValues() {
        // Arrange & Act
        SettingsInputData inputData = new SettingsInputData("abc", "def", "ghi");

        // Assert
        assertEquals("abc", inputData.getAge());
        assertEquals("def", inputData.getHeight());
        assertEquals("ghi", inputData.getWeight());
    }

    /**
     * Tests the constructor with mixed valid and empty values.
     */
    @Test
    public void testWithMixedValidAndEmptyValues() {
        // Arrange & Act
        SettingsInputData inputData = new SettingsInputData("25", "", "70");

        // Assert
        assertEquals("25", inputData.getAge());
        assertEquals("", inputData.getHeight());
        assertEquals("70", inputData.getWeight());
    }

    /**
     * Tests the constructor with whitespace values.
     */
    @Test
    public void testWithWhitespaceValues() {
        // Arrange & Act
        SettingsInputData inputData = new SettingsInputData("  ", "  ", "  ");

        // Assert
        assertEquals("  ", inputData.getAge());
        assertEquals("  ", inputData.getHeight());
        assertEquals("  ", inputData.getWeight());
    }
}
