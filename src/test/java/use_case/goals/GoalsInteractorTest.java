package use_case.goals;

import Entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GoalsInteractor covering validation paths, redirect, and success outputs.
 */
class GoalsInteractorTest {

    private FakeUserDataAccess userDataAccess;
    private FakePresenter presenter;
    private GoalsInteractor interactor;

    @BeforeEach
    void setUp() {
        this.userDataAccess = new FakeUserDataAccess();
        this.presenter = new FakePresenter();
        this.interactor = new GoalsInteractor(userDataAccess, presenter);
    }

    /**
     * Tests that interactor redirects to settings when no current user is set.
     */
    @Test
    void execute_redirectsWhenNoCurrentUser() {
        userDataAccess.setCurrentUsername(null);

        interactor.execute(new GoalsInputData("Maintain", "", "4"));

        assertTrue(presenter.redirectCalled);
        assertEquals("Please input your weight in Settings before using Goals.", presenter.lastRedirectMessage);
    }

    /**
     * Tests that interactor redirects to settings when user has no weight set.
     */
    @Test
    void execute_redirectsWhenUserHasNoWeight() {
        userDataAccess.setCurrentUsername("alice");
        userDataAccess.put("alice", new User("alice", "pwd"));

        interactor.execute(new GoalsInputData("Maintain", "", "4"));

        assertTrue(presenter.redirectCalled);
        assertEquals("Please input your weight in Settings before using Goals.", presenter.lastRedirectMessage);
    }

    /**
     * Tests that interactor fails when timeframe is empty string.
     */
    @Test
    void execute_failsWhenTimeframeEmpty() {
        userDataAccess.setCurrentUsername("bob");
        userDataAccess.put("bob", new User("bob", "pwd", 30, 180, 70));

        interactor.execute(new GoalsInputData("Maintain", "", ""));

        assertTrue(presenter.failCalled);
        assertEquals("Timeframe must be a valid whole number of weeks.", presenter.lastFailMessage);
    }

    /**
     * Tests that interactor fails when timeframe is null
     */
    @Test
    void execute_failsWhenTimeframeNull() {
        userDataAccess.setCurrentUsername("bob");
        userDataAccess.put("bob", new User("bob", "pwd", 30, 180, 70));

        interactor.execute(new GoalsInputData("Maintain", "", null));

        assertTrue(presenter.failCalled);
        assertEquals("Timeframe must be a valid whole number of weeks.", presenter.lastFailMessage);
    }

    /**
     * Tests that interactor fails when timeframe is not a valid integer.
     */
    @Test
    void execute_failsWhenTimeframeNotInteger() {
        userDataAccess.setCurrentUsername("bob");
        userDataAccess.put("bob", new User("bob", "pwd", 30, 180, 70));

        interactor.execute(new GoalsInputData("Maintain", "", "abc"));

        assertTrue(presenter.failCalled);
        assertEquals("Timeframe must be a valid whole number of weeks.", presenter.lastFailMessage);
    }

    /**
     * Tests that interactor fails when timeframe is less than one.
     */
    @Test
    void execute_failsWhenTimeframeLessThanOne() {
        userDataAccess.setCurrentUsername("bob");
        userDataAccess.put("bob", new User("bob", "pwd", 30, 180, 70));

        interactor.execute(new GoalsInputData("Maintain", "", "0"));

        assertTrue(presenter.failCalled);
        assertEquals("Timeframe must be a positive number of weeks.", presenter.lastFailMessage);
    }

    /**
     * Tests that interactor fails when target weight is not a valid number.
     */
    @Test
    void execute_failsWhenTargetNotNumber() {
        userDataAccess.setCurrentUsername("carol");
        userDataAccess.put("carol", new User("carol", "pwd", 30, 170, 65));

        interactor.execute(new GoalsInputData("Maintain", "abc", "4"));

        assertTrue(presenter.failCalled);
        assertEquals("Target weight must be a valid number.", presenter.lastFailMessage);
    }

    /**
     * Tests that interactor fails when target weight is not positive.
     */
    @Test
    void execute_failsWhenTargetNotPositive() {
        userDataAccess.setCurrentUsername("carol");
        userDataAccess.put("carol", new User("carol", "pwd", 30, 170, 65));

        interactor.execute(new GoalsInputData("Maintain", "0", "4"));

        assertTrue(presenter.failCalled);
        assertEquals("Target weight must be a positive number.", presenter.lastFailMessage);
    }

    /**
     * Tests that interactor fails when weight loss target is not less than current weight.
     */
    @Test
    void execute_failsWhenWeightLossTargetNotLessThanCurrent() {
        userDataAccess.setCurrentUsername("dave");
        userDataAccess.put("dave", new User("dave", "pwd", 30, 175, 70));

        interactor.execute(new GoalsInputData("Weight Loss", "75", "4"));

        assertTrue(presenter.failCalled);
        assertTrue(presenter.lastFailMessage.contains("For a weight loss goal"));
    }

    /**
     * Tests that interactor fails when weight gain target is not greater than current weight.
     */
    @Test
    void execute_failsWhenWeightGainTargetNotGreaterThanCurrent() {
        userDataAccess.setCurrentUsername("ellen");
        userDataAccess.put("ellen", new User("ellen", "pwd", 30, 165, 80));

        interactor.execute(new GoalsInputData("Weight Gain", "75", "4"));

        assertTrue(presenter.failCalled);
        assertTrue(presenter.lastFailMessage.contains("For a weight gain goal"));
    }

    /**
     * Tests that weight loss goal produces valid output data with correct calories and explanation.
     */
    @Test
    void execute_successWeightLossProducesOutputData() {
        userDataAccess.setCurrentUsername("frank");
        userDataAccess.put("frank", new User("frank", "pwd", 40, 180, 80));

        interactor.execute(new GoalsInputData("Weight Loss", "70", "10"));

        assertTrue(presenter.successCalled);
        GoalsOutputData data = presenter.lastSuccessData;
        assertNotNull(data);
        assertEquals("Weight Loss", data.getGoalType());
        assertEquals("70", data.getTarget());
        assertEquals("10", data.getTimeframe());
        assertTrue(data.getDailyBurnCalories() != null && !data.getDailyBurnCalories().isEmpty());
        assertTrue(data.getDailyIntakeCalories() != null && !data.getDailyIntakeCalories().isEmpty());
        assertEquals(80, data.getCurrentWeightKg());
        assertTrue(data.getExplanation().contains("lose weight"));
    }

    /**
     * Tests that weight gain goal produces correct explanation with proper caloric guidance
     */
    @Test
    void execute_successWeightGainProducesCorrectExplanation() {
        userDataAccess.setCurrentUsername("gustav");
        userDataAccess.put("gustav", new User("gustav", "pwd", 35, 175, 65));

        interactor.execute(new GoalsInputData("Weight Gain", "75", "10"));

        assertTrue(presenter.successCalled);
        GoalsOutputData data = presenter.lastSuccessData;
        assertNotNull(data);
        assertEquals("Weight Gain", data.getGoalType());
        assertEquals("75", data.getTarget());
        assertEquals("10", data.getTimeframe());
        assertTrue(data.getDailyBurnCalories() != null && !data.getDailyBurnCalories().isEmpty());
        assertTrue(data.getDailyIntakeCalories() != null && !data.getDailyIntakeCalories().isEmpty());
        assertEquals(65, data.getCurrentWeightKg());
        assertTrue(data.getExplanation().contains("gain weight"));
        assertTrue(data.getExplanation().contains("caloric surplus"));
    }

    /**
     * Tests that maintenance goal succeeds with default explanation when no target is specified.
     */
    @Test
    void execute_successDefaultGoalExplanationWhenNotLossOrGain() {
        userDataAccess.setCurrentUsername("gina");
        userDataAccess.put("gina", new User("gina", "pwd", 25, 160, 60));

        // empty target should default to current weight and succeed for non loss/gain goal
        interactor.execute(new GoalsInputData("Maintain", "", "4"));

        assertTrue(presenter.successCalled);
        GoalsOutputData data = presenter.lastSuccessData;
        assertNotNull(data);
        assertEquals("Maintain", data.getGoalType());
        assertEquals("", data.getTarget());
        assertEquals("4", data.getTimeframe());
        assertTrue(data.getExplanation().contains("Maintain your current weight") || !data.getExplanation().isEmpty());
    }

    /**
     * Tests that null target string defaults to current weight, triggering the branch on line 202.
     */
    @Test
    void execute_successWhenTargetIsNull() {
        userDataAccess.setCurrentUsername("iris");
        userDataAccess.put("iris", new User("iris", "pwd", 30, 165, 75));

        interactor.execute(new GoalsInputData("Maintain", null, "6"));

        assertTrue(presenter.successCalled);
        GoalsOutputData data = presenter.lastSuccessData;
        assertNotNull(data);
        assertEquals("Maintain", data.getGoalType());
        assertEquals(75, data.getCurrentWeightKg());
        assertTrue(data.getExplanation().contains("Maintain your current weight"));
    }

    /**
     * Tests that refreshCurrentWeight updates to zero when no username is set.
     */
    @Test
    void refreshCurrentWeight_whenNoUsername_updatesZero() {
        userDataAccess.setCurrentUsername(null);
        interactor.refreshCurrentWeight();
        assertTrue(presenter.updateCalled);
        assertEquals(0, presenter.lastUpdatedWeight);
    }

    /**
     * Tests that refreshCurrentWeight updates to zero when user does not exist in data access.
     */
    @Test
    void refreshCurrentWeight_whenUsernameButNoUser_updatesZero() {
        userDataAccess.setCurrentUsername("missingUser");
        interactor.refreshCurrentWeight();
        assertTrue(presenter.updateCalled);
        assertEquals(0, presenter.lastUpdatedWeight);
    }

    /**
     * Tests that refreshCurrentWeight correctly updates presenter with user's actual weight.
     */
    @Test
    void refreshCurrentWeight_whenUserExists_updatesWeight() {
        userDataAccess.setCurrentUsername("harry");
        userDataAccess.put("harry", new User("harry", "pwd", 28, 170, 75));
        interactor.refreshCurrentWeight();
        assertTrue(presenter.updateCalled);
        assertEquals(75, presenter.lastUpdatedWeight);
    }

    private static final class FakeUserDataAccess implements GoalsUserDataAccessInterface {
        private final java.util.Map<String, User> map = new java.util.HashMap<>();
        private String currentUsername;

        void put(String username, User u) { map.put(username, u); }
        void setCurrentUsername(String username) { this.currentUsername = username; }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }

        @Override
        public User get(String username) {
            return map.get(username);
        }
    }

    private static final class FakePresenter implements GoalsOutputBoundary {
        boolean redirectCalled = false;
        String lastRedirectMessage = null;

        boolean failCalled = false;
        String lastFailMessage = null;

        boolean successCalled = false;
        GoalsOutputData lastSuccessData = null;

        boolean updateCalled = false;
        int lastUpdatedWeight = -1;

        @Override
        public void present(GoalsOutputData outputData) {
            // not used in interactor tests
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failCalled = true;
            this.lastFailMessage = errorMessage;
        }

        @Override
        public void presentError(String errorMessage) {
            this.failCalled = true;
            this.lastFailMessage = errorMessage;
        }

        @Override
        public void redirectToSettings(String message) {
            this.redirectCalled = true;
            this.lastRedirectMessage = message;
        }

        @Override
        public void prepareSuccessView(GoalsOutputData outputData) {
            this.successCalled = true;
            this.lastSuccessData = outputData;
        }

        @Override
        public void updateCurrentWeight(int weightKg) {
            this.updateCalled = true;
            this.lastUpdatedWeight = weightKg;
        }
    }
}
