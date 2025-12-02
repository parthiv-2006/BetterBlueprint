package use_case.settings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SettingsOutputData.
 */
public class SettingsOutputDataTest {

    /**
     * Tests the constructor and getters with valid positive integer values for age, height, and weight.
     */
    @Test
    public void testConstructorAndGettersWithValidValues() {
        // Arrange
        int age = 25;
        int height = 175;
        int weight = 70;

        // Act
        SettingsOutputData outputData = new SettingsOutputData(age, height, weight);

        // Assert
        assertNotNull(outputData, "OutputData should not be null");
        assertEquals(age, outputData.getAge(), "Age should match");
        assertEquals(height, outputData.getHeight(), "Height should match");
        assertEquals(weight, outputData.getWeight(), "Weight should match");
    }

    /**
     * Tests the constructor with zero values for all fields.
     */
    @Test
    public void testConstructorWithZeroValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(0, 0, 0);

        // Assert
        assertEquals(0, outputData.getAge(), "Age should be 0");
        assertEquals(0, outputData.getHeight(), "Height should be 0");
        assertEquals(0, outputData.getWeight(), "Weight should be 0");
    }

    /**
     * Tests the constructor with typical adult values.
     */
    @Test
    public void testWithTypicalAdultValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(30, 180, 75);

        // Assert
        assertEquals(30, outputData.getAge());
        assertEquals(180, outputData.getHeight());
        assertEquals(75, outputData.getWeight());
    }

    /**
     * Tests the constructor with minimum realistic values for an adult.
     */
    @Test
    public void testWithMinimumRealisticValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(18, 150, 45);

        // Assert
        assertEquals(18, outputData.getAge());
        assertEquals(150, outputData.getHeight());
        assertEquals(45, outputData.getWeight());
    }

    /**
     * Tests the constructor with high values (elderly person, tall height, heavy weight).
     */
    @Test
    public void testWithHighValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(85, 200, 120);

        // Assert
        assertEquals(85, outputData.getAge());
        assertEquals(200, outputData.getHeight());
        assertEquals(120, outputData.getWeight());
    }

    /**
     * Tests that getters return consistent values when called multiple times.
     */
    @Test
    public void testGettersReturnConsistentValues() {
        // Arrange
        int age = 28;
        int height = 170;
        int weight = 68;
        SettingsOutputData outputData = new SettingsOutputData(age, height, weight);

        // Act & Assert - Call getters multiple times to ensure consistency
        for (int i = 0; i < 3; i++) {
            assertEquals(age, outputData.getAge(), "Age should remain consistent");
            assertEquals(height, outputData.getHeight(), "Height should remain consistent");
            assertEquals(weight, outputData.getWeight(), "Weight should remain consistent");
        }
    }

    /**
     * Tests the constructor with young adult values.
     */
    @Test
    public void testWithYoungAdultValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(20, 165, 60);

        // Assert
        assertEquals(20, outputData.getAge());
        assertEquals(165, outputData.getHeight());
        assertEquals(60, outputData.getWeight());
    }

    /**
     * Tests the constructor with middle-aged adult values.
     */
    @Test
    public void testWithMiddleAgedValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(45, 172, 80);

        // Assert
        assertEquals(45, outputData.getAge());
        assertEquals(172, outputData.getHeight());
        assertEquals(80, outputData.getWeight());
    }

    /**
     * Tests the constructor with values for a shorter, lighter person.
     */
    @Test
    public void testWithShorterLighterPersonValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(22, 155, 50);

        // Assert
        assertEquals(22, outputData.getAge());
        assertEquals(155, outputData.getHeight());
        assertEquals(50, outputData.getWeight());
    }

    /**
     * Tests the constructor with values for a taller, heavier person.
     */
    @Test
    public void testWithTallerHeavierPersonValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(35, 195, 110);

        // Assert
        assertEquals(35, outputData.getAge());
        assertEquals(195, outputData.getHeight());
        assertEquals(110, outputData.getWeight());
    }

    /**
     * Tests the constructor with negative values (edge case that should be handled by validation elsewhere).
     */
    @Test
    public void testWithNegativeValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(-5, -180, -70);

        // Assert
        assertEquals(-5, outputData.getAge());
        assertEquals(-180, outputData.getHeight());
        assertEquals(-70, outputData.getWeight());
    }

    /**
     * Tests the constructor with very large values (edge case).
     */
    @Test
    public void testWithVeryLargeValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(120, 300, 500);

        // Assert
        assertEquals(120, outputData.getAge());
        assertEquals(300, outputData.getHeight());
        assertEquals(500, outputData.getWeight());
    }

    /**
     * Tests the constructor with typical fitness enthusiast values.
     */
    @Test
    public void testWithFitnessEnthusiastValues() {
        // Arrange & Act
        SettingsOutputData outputData = new SettingsOutputData(26, 178, 82);

        // Assert
        assertEquals(26, outputData.getAge());
        assertEquals(178, outputData.getHeight());
        assertEquals(82, outputData.getWeight());
    }
}
