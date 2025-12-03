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
     * Tests that providing only height and weight (with empty age) is accepted.
     * With the new optional fields feature, users can update any combination of fields.
     * Expected: Success view is prepared, height and weight are updated.
     */
    @Test
    void testEmptyAge() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("", "175", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedUser, "User should be saved");
        assertEquals(175, mockDataAccess.savedUser.getHeight());
        assertEquals(70, mockDataAccess.savedUser.getWeight());
    }

    /**
     * Tests that providing only age and weight (with empty height) is accepted.
     * With the new optional fields feature, users can update any combination of fields.
     * Expected: Success view is prepared, age and weight are updated.
     */
    @Test
    void testEmptyHeight() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedUser, "User should be saved");
        assertEquals(25, mockDataAccess.savedUser.getAge());
        assertEquals(70, mockDataAccess.savedUser.getWeight());
    }

    /**
     * Tests that providing only age and height (with empty weight) is accepted.
     * With the new optional fields feature, users can update any combination of fields.
     * Expected: Success view is prepared, age and height are updated.
     */
    @Test
    void testEmptyWeight() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("25", "175", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedUser, "User should be saved");
        assertEquals(25, mockDataAccess.savedUser.getAge());
        assertEquals(175, mockDataAccess.savedUser.getHeight());
    }

    /**
     * Tests that providing only age (with empty height and weight) is accepted.
     * With the new optional fields feature, users can update just one field.
     * Expected: Success view is prepared, only age is updated, height and weight remain as existing values.
     */
    @Test
    void testOnlyAgeProvided() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("30", "", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedUser, "User should be saved");
        assertEquals(30, mockDataAccess.savedUser.getAge(), "Age should be updated");
    }

    /**
     * Tests that providing only height (with empty age and weight) is accepted.
     * With the new optional fields feature, users can update just one field.
     * Expected: Success view is prepared, only height is updated.
     */
    @Test
    void testOnlyHeightProvided() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("", "180", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedUser, "User should be saved");
        assertEquals(180, mockDataAccess.savedUser.getHeight(), "Height should be updated");
    }

    /**
     * Tests that providing only weight (with empty age and height) is accepted.
     * With the new optional fields feature, users can update just one field.
     * Expected: Success view is prepared, only weight is updated.
     */
    @Test
    void testOnlyWeightProvided() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("", "", "75");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertNotNull(mockDataAccess.savedUser, "User should be saved");
        assertEquals(75, mockDataAccess.savedUser.getWeight(), "Weight should be updated");
    }

    /**
     * Tests that providing no fields (all empty) is properly rejected.
     * At least one field must be provided to perform an update.
     * Expected: Fail view is prepared with error message.
     */
    @Test
    void testAllFieldsEmpty() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("", "", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("at least one field"));
    }

    /**
     * Tests that non-numeric input for age is properly rejected.
     * When the age field contains text that cannot be parsed as an integer,
     * the interactor should catch the NumberFormatException and report an error.
     * Expected: Fail view is prepared with error message about valid numbers for provided fields.
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
     * Expected: Fail view is prepared with error message about valid numbers for provided fields.
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
     * Expected: Fail view is prepared with error message about valid numbers for provided fields.
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
        assertTrue(mockPresenter.errorMessage.contains("Age") &&
                   mockPresenter.errorMessage.contains("positive"),
                   "Error message should mention Age must be positive");
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
        assertTrue(mockPresenter.errorMessage.contains("Height") &&
                   mockPresenter.errorMessage.contains("positive"),
                   "Error message should mention Height must be positive");
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
        assertTrue(mockPresenter.errorMessage.contains("Weight") &&
                   mockPresenter.errorMessage.contains("positive"),
                   "Error message should mention Weight must be positive");
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
        assertTrue(mockPresenter.errorMessage.contains("Age") &&
                   mockPresenter.errorMessage.contains("positive"),
                   "Error message should mention Age must be positive");
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
        assertTrue(mockPresenter.errorMessage.contains("Height") &&
                   mockPresenter.errorMessage.contains("positive"),
                   "Error message should mention Height must be positive");
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
        assertTrue(mockPresenter.errorMessage.contains("Weight") &&
                   mockPresenter.errorMessage.contains("positive"),
                   "Error message should mention Weight must be positive");
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
     * Tests providing age and height together (weight empty).
     * Users should be able to update any two fields while leaving the third empty.
     * Expected: Success view is prepared, age and height are updated.
     */
    @Test
    void testAgeAndHeightProvided() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("28", "172", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertEquals(28, mockDataAccess.savedUser.getAge());
        assertEquals(172, mockDataAccess.savedUser.getHeight());
    }

    /**
     * Tests providing age and weight together (height empty).
     * Users should be able to update any two fields while leaving the third empty.
     * Expected: Success view is prepared, age and weight are updated.
     */
    @Test
    void testAgeAndWeightProvided() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("35", "", "82");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertEquals(35, mockDataAccess.savedUser.getAge());
        assertEquals(82, mockDataAccess.savedUser.getWeight());
    }

    /**
     * Tests providing height and weight together (age empty).
     * Users should be able to update any two fields while leaving the third empty.
     * Expected: Success view is prepared, height and weight are updated.
     */
    @Test
    void testHeightAndWeightProvided() {
        // Arrange
        SettingsInputData inputData = new SettingsInputData("", "165", "68");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertEquals(165, mockDataAccess.savedUser.getHeight());
        assertEquals(68, mockDataAccess.savedUser.getWeight());
    }

    /**
     * Tests that whitespace-only fields are treated as empty.
     * Fields containing only spaces, tabs, or other whitespace should be ignored.
     * Expected: Only the non-whitespace field (age) is updated.
     */
    @Test
    void testWhitespaceOnlyFieldsTreatedAsEmpty() {
        // Arrange - age has value, height and weight have only whitespace
        SettingsInputData inputData = new SettingsInputData("40", "   ", "\t");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertFalse(mockPresenter.isFailCalled, "Fail view should not be prepared");
        assertEquals(40, mockDataAccess.savedUser.getAge());
    }

    /**
     * Tests that fields with leading/trailing whitespace are trimmed and parsed correctly.
     * The interactor should handle inputs like " 25 " properly by trimming whitespace.
     * Expected: Success view is prepared with correctly parsed values.
     */
    @Test
    void testFieldsWithWhitespaceAreTrimmed() {
        // Arrange - values have leading/trailing whitespace
        SettingsInputData inputData = new SettingsInputData(" 25 ", " 175 ", " 70 ");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessCalled, "Success view should be prepared");
        assertEquals(25, mockDataAccess.savedUser.getAge());
        assertEquals(175, mockDataAccess.savedUser.getHeight());
        assertEquals(70, mockDataAccess.savedUser.getWeight());
    }

    /**
     * Tests that decimal/floating-point inputs are properly rejected.
     * The system expects integer values only. When users enter decimal values
     * (e.g., 25.5, 175.5, 70.5), the parsing should fail with a NumberFormatException
     * since Integer.parseInt() cannot parse decimal numbers.
     * Expected: Fail view is prepared with error message about valid numbers for provided fields.
     */
    @Test
    void testDecimalInputsAreRejected() {
        // Arrange - decimal values should fail parsing
        SettingsInputData inputData = new SettingsInputData("25.5", "175.5", "70.5");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Decimal values should be rejected");
        assertTrue(mockPresenter.errorMessage.contains("valid numbers") &&
                   mockPresenter.errorMessage.contains("provided"));
    }

    /**
     * Tests that age exceeding the maximum value (>150) triggers the IllegalArgumentException
     * from the User entity validation. This tests the catch block at line 82 of SettingsInteractor.
     * The User entity validates that age must be between 0 and 150.
     * Expected: Fail view is prepared with the entity's validation error message.
     */
    @Test
    void testAgeExceedsMaximum() {
        // Arrange - age of 151 exceeds the User entity's maximum of 150
        SettingsInputData inputData = new SettingsInputData("151", "175", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared for age > 150");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Age must be between 0 and 150") ||
                   mockPresenter.errorMessage.contains("Age"),
                   "Error message should mention age validation");
    }

    /**
     * Tests that a very large age value triggers the IllegalArgumentException
     * from the User entity validation.
     * Expected: Fail view is prepared with the entity's validation error message.
     */
    @Test
    void testVeryLargeAge() {
        // Arrange - age of 200 is well beyond the maximum
        SettingsInputData inputData = new SettingsInputData("200", "180", "75");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Age must be between 0 and 150") ||
                   mockPresenter.errorMessage.contains("Age"),
                   "Error message should mention age validation");
    }

    /**
     * Tests that null values in input fields are handled as empty strings.
     * Expected: Treated as empty fields, at least one field required error.
     */
    @Test
    void testNullInputFields() {
        // Arrange - all fields are null
        SettingsInputData inputData = new SettingsInputData(null, null, null);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("at least one field"),
                   "Error message should indicate at least one field is required");
    }

    /**
     * Tests mixed null and empty string inputs.
     * Expected: Treated as all empty, at least one field required error.
     */
    @Test
    void testMixedNullAndEmptyFields() {
        // Arrange - mix of null and empty
        SettingsInputData inputData = new SettingsInputData(null, "", null);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("at least one field"));
    }

    /**
     * Tests that special characters in input are properly rejected.
     * Expected: NumberFormatException caught, fail view prepared.
     */
    @Test
    void testSpecialCharactersInAge() {
        // Arrange - special characters that can't be parsed
        SettingsInputData inputData = new SettingsInputData("@#$", "175", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("valid numbers"));
    }

    /**
     * Tests that alphanumeric combinations are properly rejected.
     * Expected: NumberFormatException caught, fail view prepared.
     */
    @Test
    void testAlphanumericHeight() {
        // Arrange - alphanumeric input
        SettingsInputData inputData = new SettingsInputData("25", "175cm", "70");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("valid numbers"));
    }

    /**
     * Tests empty strings with spaces for all fields.
     * Expected: All treated as empty, at least one field required error.
     */
    @Test
    void testAllFieldsWithOnlySpaces() {
        // Arrange - all fields contain only spaces
        SettingsInputData inputData = new SettingsInputData("   ", "   ", "   ");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("at least one field"));
    }

    /**
     * Tests that zero age with other valid fields is rejected at the individual field validation level.
     * Expected: Fail view with age-specific error message.
     */
    @Test
    void testZeroAgeOnly() {
        // Arrange - only age is zero, others are empty
        SettingsInputData inputData = new SettingsInputData("0", "", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Age") &&
                   mockPresenter.errorMessage.contains("positive"));
    }

    /**
     * Tests that zero height with other valid fields is rejected at the individual field validation level.
     * Expected: Fail view with height-specific error message.
     */
    @Test
    void testZeroHeightOnly() {
        // Arrange - only height is zero, others are empty
        SettingsInputData inputData = new SettingsInputData("", "0", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Height") &&
                   mockPresenter.errorMessage.contains("positive"));
    }

    /**
     * Tests that zero weight with other valid fields is rejected at the individual field validation level.
     * Expected: Fail view with weight-specific error message.
     */
    @Test
    void testZeroWeightOnly() {
        // Arrange - only weight is zero, others are empty
        SettingsInputData inputData = new SettingsInputData("", "", "0");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Weight") &&
                   mockPresenter.errorMessage.contains("positive"));
    }

    /**
     * Tests negative age when it's the only field provided.
     * Expected: Fail view with age-specific error message.
     */
    @Test
    void testNegativeAgeOnly() {
        // Arrange - only age is provided and it's negative
        SettingsInputData inputData = new SettingsInputData("-10", "", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Age") &&
                   mockPresenter.errorMessage.contains("positive"));
    }

    /**
     * Tests negative height when it's the only field provided.
     * Expected: Fail view with height-specific error message.
     */
    @Test
    void testNegativeHeightOnly() {
        // Arrange - only height is provided and it's negative
        SettingsInputData inputData = new SettingsInputData("", "-180", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Height") &&
                   mockPresenter.errorMessage.contains("positive"));
    }

    /**
     * Tests negative weight when it's the only field provided.
     * Expected: Fail view with weight-specific error message.
     */
    @Test
    void testNegativeWeightOnly() {
        // Arrange - only weight is provided and it's negative
        SettingsInputData inputData = new SettingsInputData("", "", "-75");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.isSuccessCalled, "Success view should not be prepared");
        assertTrue(mockPresenter.errorMessage.contains("Weight") &&
                   mockPresenter.errorMessage.contains("positive"));
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

