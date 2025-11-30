package use_case.goals;

import Entities.User;

public class GoalsInteractor implements GoalsInputBoundary {

    private final GoalsOutputBoundary presenter;
    private final GoalsUserDataAccessInterface userDataAccess;

    public GoalsInteractor(GoalsUserDataAccessInterface userDataAccess,
                           GoalsOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(GoalsInputData inputData) {
        // 1) Get current user through the use-case-specific interface
        String username = userDataAccess.getCurrentUsername();
        User user = (username != null) ? userDataAccess.get(username) : null;

        if (user == null || user.getWeight() <= 0) {
            presenter.redirectToSettings("Please input your weight in Settings before using Goals.");
            return;
        }

        int currentWeight = user.getWeight();
        int age = user.getAge();
        int height = user.getHeight();

        int timeframe = Integer.parseInt(inputData.getTimeframe());
        String goalType = inputData.getGoalType();

        double bmr = 10 * currentWeight + 6.25 * height - 5 * age + 5;
        double dailyBurn = bmr * 1.5;

        double targetWeight = inputData.getTarget().isEmpty()
                ? currentWeight
                : Double.parseDouble(inputData.getTarget());

        double weeklyWeightChange = (targetWeight - currentWeight) / timeframe;
        double dailyCalorieAdjustment = (weeklyWeightChange * 7700) / 7.0;
        double dailyIntake = dailyBurn + dailyCalorieAdjustment;

        String explanation = generateExplanation(goalType);

        GoalsOutputData outputData = new GoalsOutputData(
                goalType,
                String.format("%.0f", dailyIntake),
                String.format("%.0f", dailyBurn),
                explanation,
                currentWeight,
                false,
                ""
        );

        presenter.present(outputData);
    }

    private String generateExplanation(String goalType) {
        if ("Weight Loss".equals(goalType)) {
            return "To lose weight, maintain a caloric deficit by consuming less than your daily burn.";
        } else if ("Weight Gain".equals(goalType)) {
            return "To gain weight, maintain a caloric surplus by consuming more than your daily burn.";
        }
        return "Maintain your current weight by consuming approximately your daily burn calories.";
    }
}
