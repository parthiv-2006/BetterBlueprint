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
        String username = userDataAccess.getCurrentUsername();
        User user = (username != null) ? userDataAccess.get(username) : null;

        if (user == null || user.getWeight() <= 0) {
            presenter.redirectToSettings(
                    "Please input your weight in Settings before using Goals."
            );
            return;
        }

        int currentWeight = user.getWeight();
        int age = user.getAge();
        int height = user.getHeight();

        String timeframeStr = inputData.getTimeframe();
        String goalType = inputData.getGoalType();
        String targetStr = inputData.getTarget();

        // 2) Parse timeframe (must be positive integer)
        final int timeframe;
        try {
            timeframe = Integer.parseInt(timeframeStr.trim());
            if (timeframe <= 0) {
                presenter.prepareFailView("Timeframe must be a positive number of weeks.");
                return;
            }
        } catch (NumberFormatException e) {
            presenter.prepareFailView("Timeframe must be a valid whole number of weeks.");
            return;
        }

        double bmr = 10 * currentWeight + 6.25 * height - 5 * age + 5;
        double dailyBurn = bmr * 1.5;

        final double targetWeight;
        if (targetStr == null || targetStr.trim().isEmpty()) {
            targetWeight = currentWeight;
        } else {
            try {
                targetWeight = Double.parseDouble(targetStr.trim());
                if (targetWeight <= 0) {
                    presenter.prepareFailView("Target weight must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                presenter.prepareFailView("Target weight must be a valid number.");
                return;
            }
        }

        // 5) Goal-specific logical validation
        if ("Weight Loss".equals(goalType) && targetWeight >= currentWeight) {
            presenter.prepareFailView(
                    "For a weight loss goal, your target weight must be LESS than your "
                            + "current weight (" + currentWeight + " kg)."
            );
            return;
        }

        if ("Weight Gain".equals(goalType) && targetWeight <= currentWeight) {
            presenter.prepareFailView(
                    "For a weight gain goal, your target weight must be GREATER than your "
                            + "current weight (" + currentWeight + " kg)."
            );
            return;
        }

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
                "",
                targetStr,
                timeframeStr
        );

        presenter.prepareSuccessView(outputData);
    }

    private String generateExplanation(String goalType) {
        if ("Weight Loss".equals(goalType)) {
            return "To lose weight, maintain a caloric deficit by consuming less than your daily burn.";
        } else if ("Weight Gain".equals(goalType)) {
            return "To gain weight, maintain a caloric surplus by consuming more than your daily burn.";
        }
        return "Maintain your current weight by consuming approximately your daily burn calories.";
    }

    @Override
    public void refreshCurrentWeight() {
        String username = userDataAccess.getCurrentUsername();
        if (username != null) {
            User user = userDataAccess.get(username);
            if (user != null) {
                presenter.updateCurrentWeight(user.getWeight());
                return;
            }
        }
        presenter.updateCurrentWeight(0);
    }
}
