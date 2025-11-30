package use_case.goals;

import Entities.User;

/**
 * GoalsInteractor:
 * - Fetches the current user from the DAO
 * - Validates that age/height/weight are set
 * - Computes daily burn and daily intake based on the goal
 * - Uses the presenter to either show results or redirect to Settings
 */
public class GoalsInteractor implements GoalsInputBoundary {

    private final GoalsUserDataAccessInterface userDataAccessObject;
    private final GoalsOutputBoundary presenter;

    public GoalsInteractor(GoalsUserDataAccessInterface userDataAccessObject,
                           GoalsOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(GoalsInputData inputData) {
        // 1. Get current user
        String username = userDataAccessObject.getCurrentUsername();
        if (username == null) {
            presenter.redirectToSettings(
                    "Please log in and set your weight in Settings before using Goals.");
            return;
        }

        User currentUser = userDataAccessObject.get(username);
        if (currentUser == null) {
            presenter.redirectToSettings(
                    "Please set your profile (age, height, weight) in Settings before using Goals.");
            return;
        }

        int weight = currentUser.getWeight();
        int age = currentUser.getAge();
        int height = currentUser.getHeight();

        // Require all core metrics
        if (weight <= 0 || age <= 0 || height <= 0) {
            presenter.redirectToSettings(
                    "Please set your age, height, and weight in Settings before using Goals.");
            return;
        }

        // 2. Parse timeframe safely
        int timeframeWeeks;
        try {
            timeframeWeeks = Integer.parseInt(inputData.getTimeframe().trim());
        } catch (NumberFormatException e) {
            // Fallback to 1 week to avoid division by zero / crash
            timeframeWeeks = 1;
        }
        if (timeframeWeeks <= 0) {
            timeframeWeeks = 1;
        }

        String goalType = inputData.getGoalType();

        // 3. Calculate BMR (Mifflin-St Jeor, male-ish baseline)
        double bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        double dailyBurn = bmr * 1.5; // assume moderate activity

        // 4. Target weight (empty string means maintain current weight)
        double targetWeight = inputData.getTarget().isEmpty()
                ? weight
                : Double.parseDouble(inputData.getTarget().trim());

        double weeklyWeightChange = (targetWeight - weight) / timeframeWeeks;
        // 1 kg ≈ 7700 calories
        double dailyCalorieAdjustment = (weeklyWeightChange * 7700) / 7.0;
        double dailyIntake = dailyBurn + dailyCalorieAdjustment;

        // 5. Build explanation text
        String explanation = generateExplanation(goalType);

        // 6. Send result to presenter
        GoalsOutputData outputData = new GoalsOutputData(
                String.format("%.0f", dailyIntake),
                String.format("%.0f", dailyBurn),
                explanation
        );
        presenter.present(outputData);
    }

    private String generateExplanation(String goalType) {
        if ("Weight Loss".equals(goalType)) {
            return "To lose weight, maintain a caloric deficit by consuming less than your daily burn.";
        } else if ("Weight Gain".equals(goalType)) {
            return "To gain weight, maintain a caloric surplus by consuming more than your daily burn.";
        }
        return "To maintain your current weight, consume approximately your daily burn calories.";
    }
}
