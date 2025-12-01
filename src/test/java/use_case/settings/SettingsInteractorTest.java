package use_case.settings;

import Entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SettingsInteractor.
 * <p>
 * This test suite validates the business logic for updating user settings (age, height, weight).
 * It tests various scenarios including:
 * - Valid inputs with typical and boundary values
 * - Invalid inputs (empty fields, non-numeric values, negative values, zero values)
 * - Input format validation (decimal numbers)
 * - Navigation functionality (switching back to home view)
 * <p>
 * Uses mock implementations of SettingsUserDataAccessInterface and SettingsOutputBoundary
 * to isolate the interactor logic from external dependencies.
 */
class SettingsInteractorTest {

    private MockSettingsDataAccess mockDataAccess;
    private MockSettingsPresenter mockPresenter;
    private SettingsInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDataAccess = new MockSettingsDataAccess();
        mockPresenter = new MockSettingsPresenter();
        interactor = new SettingsInteractor(mockDataAccess, mockPresenter);
    }

    /**
     * Tests that valid age, height, and weight values are successfully processed.
     * When all three fields contain valid positive integers, the user object should be
     * updated with the new values and saved to the data access layer.
     * Expected: Success view is prepared, user is saved with correct values.
     */
    @Test
    void testValidSettings() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "175", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedUser, "User should be saved");
        assertEquals(25, mockDataAccess.savedUser.getAge());
        assertEquals(175, mockDataAccess.savedUser.getHeight());
        assertEquals(70, mockDataAccess.savedUser.getWeight());
    }

    /**
     * Tests that an empty age field is properly rejected.
     * Validates that the interactor enforces the requirement that all fields must be filled.
     * Expected: Fail view is prepared with error message indicating all fields must be filled.
     */
    @Test
    void testEmptyAge() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("", "175", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("All fields must be filled"));
    }

    /**
     * Tests that an empty height field is properly rejected.
     * Validates that the interactor enforces the requirement that all fields must be filled.
     * Expected: Fail view is prepared with error message indicating all fields must be filled.
     */
    @Test
    void testEmptyHeight() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("All fields must be filled"));
    }

    /**
     * Tests that an empty weight field is properly rejected.
     * Validates that the interactor enforces the requirement that all fields must be filled.
     * Expected: Fail view is prepared with error message indicating all fields must be filled.
     */
    @Test
    void testEmptyWeight() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "175", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("All fields must be filled"));
    }

    /**
     * Tests that non-numeric input for age is properly rejected.
     * When the age field contains text that cannot be parsed as an integer,
     * the interactor should catch the NumberFormatException and report an error.
     * Expected: Fail view is prepared with error message about valid numbers.
     */
    @Test
    void testInvalidAge_NotANumber() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("abc", "175", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("valid numbers"));
    }

    /**
     * Tests that non-numeric input for height is properly rejected.
     * When the height field contains text that cannot be parsed as an integer,
     * the interactor should catch the NumberFormatException and report an error.
     * Expected: Fail view is prepared with error message about valid numbers.
     */
    @Test
    void testInvalidHeight_NotANumber() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "abc", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("valid numbers"));
    }

    /**
     * Tests that non-numeric input for weight is properly rejected.
     * When the weight field contains text that cannot be parsed as an integer,
     * the interactor should catch the NumberFormatException and report an error.
     * Expected: Fail view is prepared with error message about valid numbers.
     */
    @Test
    void testInvalidWeight_NotANumber() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "175", "abc");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("valid numbers"));
    }

    @Test
    void testNegativeAge() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("-5", "175", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("positive numbers"));
    }

    @Test
    void testNegativeHeight() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "-175", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("positive numbers"));
    }

    @Test
    void testNegativeWeight() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "175", "-70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("positive numbers"));
    }

    @Test
    void testZeroAge() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("0", "175", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("positive numbers"));
    }

    @Test
    void testZeroHeight() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "0", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("positive numbers"));
    }

    @Test
    void testZeroWeight() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "175", "0");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("positive numbers"));
    }

    /**
     * Tests the minimum valid boundary values for all fields.
     * Validates that the smallest acceptable positive values (1) for age, height, and weight
     * are processed successfully. This is an edge case test to ensure the validation
     * correctly accepts the minimum valid values.
     * Expected: Success view is prepared, user is saved with values of 1.
     */
    @Test
    void testBoundaryValues_MinimumValid() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("1", "1", "1");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertEquals(1, mockDataAccess.savedUser.getAge());
        assertEquals(1, mockDataAccess.savedUser.getHeight());
        assertEquals(1, mockDataAccess.savedUser.getWeight());
    }

    /**
     * Tests the maximum valid boundary value for age (150).
     * According to the User entity validation, age must be between 0 and 150.
     * This test verifies that the maximum age of 150 is accepted while also testing
     * with large but valid height and weight values.
     * Expected: Success view is prepared, user is saved with age of 150.
     */
    @Test
    void testBoundaryValues_MaximumValidAge() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("150", "250", "500");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertEquals(150, mockDataAccess.savedUser.getAge());
    }


    /**
     * Tests typical real-world user settings values.
     * This test uses realistic values that a typical user might enter:
     * - Age: 30 years
     * - Height: 180 cm
     * - Weight: 75 kg
     * Validates that common, everyday inputs are processed correctly.
     * Expected: Success view is prepared with the typical values saved correctly.
     */
    @Test
    void testTypicalUserSettings() {
        // Arrange - typical real-world values
        SettingsInputData inputData = new SettingsInputData("30", "180", "75");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled);
        assertEquals(30, mockDataAccess.savedUser.getAge());
        assertEquals(180, mockDataAccess.savedUser.getHeight());
        assertEquals(75, mockDataAccess.savedUser.getWeight());
    }

    /**
     * Tests that decimal/floating-point inputs are properly rejected.
     * The system expects integer values only. When users enter decimal values
     * (e.g., 25.5, 175.5, 70.5), the parsing should fail with a NumberFormatException
     * since Integer.parseInt() cannot parse decimal numbers.
     * Expected: Fail view is prepared with error message about valid numbers.
     */
    @Test
    void testDecimalInputsAreRejected() {
        // Arrange - decimal values should fail parsing
        SettingsInputData inputData = new SettingsInputData("25.5", "175.5", "70.5");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Decimal values should be rejected");
        assertTrue(mockPresenter.errorMessage.contains("valid numbers"));
    }

    // Mock classes for testing

    /**
     * Mock implementation of SettingsUserDataAccessInterface for testing.
     * Simulates data access layer without requiring actual file/database operations.
     * Tracks saved user objects for verification in tests.
     */
    private static class MockSettingsDataAccess implements SettingsUserDataAccessInterface {
        User savedUser;
        String currentUsername = "testuser";
        User currentUser = new User("testuser", "password123");

        @Override
        public User get(String username) {
            return currentUser;
        }

        @Override
        public void save(User user) {
            this.savedUser = user;
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }
    }

    /**
     * Mock implementation of SettingsOutputBoundary for testing.
     * Tracks which presenter methods were called and captures error messages
     * for verification in tests. Does not perform actual view updates.
     */
    private static class MockSettingsPresenter implements SettingsOutputBoundary {
        boolean isSuccessCalled = false;
        boolean isFailCalled = false;
        String errorMessage = "";
        SettingsOutputData outputData;

        @Override
        public void prepareSuccessView(SettingsOutputData outputData) {
            this.isSuccessCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.isFailCalled = true;
            this.errorMessage = errorMessage;
        }
    }
}

