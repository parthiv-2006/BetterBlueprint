package use_case.goals;

import Entities.User;

public class GoalsInteractor implements GoalsInputBoundary {

    private static final String GOAL_TYPE_WEIGHT_LOSS = "Weight Loss";
    private static final String GOAL_TYPE_WEIGHT_GAIN = "Weight Gain";

    private final GoalsOutputBoundary presenter;
    private final GoalsUserDataAccessInterface userDataAccess;

    public GoalsInteractor(final GoalsUserDataAccessInterface userDataAccess,
                           final GoalsOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(final GoalsInputData inputData) {
        final User user = getCurrentUser();
        final GoalsValidationResult result =
                GoalsValidator.validate(user, inputData);

        if (!result.isValid()) {
            if (result.isRedirectToSettings()) {
                presenter.redirectToSettings(result.getErrorMessage());
            } else {
                presenter.prepareFailView(result.getErrorMessage());
            }
        } else {
            final double dailyBurn = calculateDailyBurn(
                    result.getCurrentWeight(),
                    result.getAge(),
                    result.getHeight()
            );

            final double dailyIntake = calculateDailyIntake(
                    result.getCurrentWeight(),
                    result.getTargetWeight(),
                    result.getTimeframe(),
                    dailyBurn
            );

            final GoalsOutputData outputData = buildOutputData(
                    result.getGoalType(),
                    dailyIntake,
                    dailyBurn,
                    result.getCurrentWeight(),
                    result.getRawTarget(),
                    result.getRawTimeframe()
            );

            presenter.prepareSuccessView(outputData);
        }
    }

    private User getCurrentUser() {
        final String username = userDataAccess.getCurrentUsername();
        if (username == null) {
            return null;
        }
        return userDataAccess.get(username);
    }

    private double calculateDailyBurn(final int currentWeight,
                                      final int age,
                                      final int height) {
        final double bmr = 10.0 * currentWeight
                + 6.25 * height
                - 5.0 * age
                + 5.0;
        return bmr * 1.5;
    }

    private double calculateDailyIntake(final int currentWeight,
                                        final double targetWeight,
                                        final int timeframe,
                                        final double dailyBurn) {
        final double weeklyWeightChange =
                (targetWeight - currentWeight) / timeframe;
        final double dailyCalorieAdjustment =
                (weeklyWeightChange * 7700.0) / 7.0;
        return dailyBurn + dailyCalorieAdjustment;
    }

    private GoalsOutputData buildOutputData(final String goalType,
                                            final double dailyIntake,
                                            final double dailyBurn,
                                            final int currentWeight,
                                            final String targetStr,
                                            final String timeframeStr) {
        final String explanation = generateExplanation(goalType);

        return new GoalsOutputData(
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
    }

    private String generateExplanation(final String goalType) {
        if (GOAL_TYPE_WEIGHT_LOSS.equals(goalType)) {
            return "To lose weight, maintain a caloric deficit by consuming "
                    + "less than your daily burn.";
        } else if (GOAL_TYPE_WEIGHT_GAIN.equals(goalType)) {
            return "To gain weight, maintain a caloric surplus by consuming "
                    + "more than your daily burn.";
        }
        return "Maintain your current weight by consuming approximately your "
                + "daily burn calories.";
    }

    @Override
    public void refreshCurrentWeight() {
        final String username = userDataAccess.getCurrentUsername();
        if (username == null) {
            presenter.updateCurrentWeight(0);
        } else {
            final User user = userDataAccess.get(username);
            if (user == null) {
                presenter.updateCurrentWeight(0);
            } else {
                presenter.updateCurrentWeight(user.getWeight());
            }
        }
    }

    /**
     * Pure validation for the Goals use case.
     * It parses strings, checks numeric ranges, and enforces goal rules.
     * It does NOT talk to the presenter.
     */
    private static final class GoalsValidator {

        private GoalsValidator() {
            // utility class
        }

        static GoalsValidationResult validate(final User user,
                                              final GoalsInputData inputData) {

            // 1) User check
            if (user == null || user.getWeight() <= 0) {
                return GoalsValidationResult.redirect(
                        "Please input your weight in Settings before using Goals."
                );
            }

            final int currentWeight = user.getWeight();
            final int age = user.getAge();
            final int height = user.getHeight();

            final String timeframeStr = inputData.getTimeframe();
            final String targetStr = inputData.getTarget();
            final String goalType = inputData.getGoalType();

            // 2) Timeframe validation
            if (timeframeStr == null || timeframeStr.trim().isEmpty()) {
                return GoalsValidationResult.fail(
                        "Timeframe must be a valid whole number of weeks."
                );
            }

            final int timeframe;
            try {
                timeframe = Integer.parseInt(timeframeStr.trim());
            } catch (NumberFormatException exception) {
                return GoalsValidationResult.fail(
                        "Timeframe must be a valid whole number of weeks."
                );
            }

            if (timeframe <= 0) {
                return GoalsValidationResult.fail(
                        "Timeframe must be a positive number of weeks."
                );
            }

            // 3) Target weight validation
            final double targetWeight;
            if (targetStr == null || targetStr.trim().isEmpty()) {
                targetWeight = currentWeight;
            } else {
                try {
                    targetWeight = Double.parseDouble(targetStr.trim());
                } catch (NumberFormatException exception) {
                    return GoalsValidationResult.fail(
                            "Target weight must be a valid number."
                    );
                }

                if (targetWeight <= 0) {
                    return GoalsValidationResult.fail(
                            "Target weight must be a positive number."
                    );
                }
            }

            // 4) Goal-versus-weight rules
            if (GOAL_TYPE_WEIGHT_LOSS.equals(goalType)
                    && targetWeight >= currentWeight) {
                return GoalsValidationResult.fail(
                        "For a weight loss goal, your target weight must be LESS than your "
                                + "current weight (" + currentWeight + " kg)."
                );
            }

            if (GOAL_TYPE_WEIGHT_GAIN.equals(goalType)
                    && targetWeight <= currentWeight) {
                return GoalsValidationResult.fail(
                        "For a weight gain goal, your target weight must be GREATER than your "
                                + "current weight (" + currentWeight + " kg)."
                );
            }

            // All good → bundle what execute needs.
            return GoalsValidationResult.success(
                    currentWeight,
                    age,
                    height,
                    timeframe,
                    targetWeight,
                    goalType,
                    targetStr,
                    timeframeStr
            );
        }
    }

    /**
     * Small DTO summarising validation result plus the parsed values needed
     * by the interactor. No behaviour, just data.
     */
    private static final class GoalsValidationResult {

        private final boolean valid;
        private final boolean redirectToSettings;
        private final String errorMessage;

        private final int currentWeight;
        private final int age;
        private final int height;
        private final int timeframe;
        private final double targetWeight;

        private final String goalType;
        private final String rawTarget;
        private final String rawTimeframe;

        private GoalsValidationResult(final boolean valid,
                                      final boolean redirectToSettings,
                                      final String errorMessage,
                                      final int currentWeight,
                                      final int age,
                                      final int height,
                                      final int timeframe,
                                      final double targetWeight,
                                      final String goalType,
                                      final String rawTarget,
                                      final String rawTimeframe) {
            this.valid = valid;
            this.redirectToSettings = redirectToSettings;
            this.errorMessage = errorMessage;
            this.currentWeight = currentWeight;
            this.age = age;
            this.height = height;
            this.timeframe = timeframe;
            this.targetWeight = targetWeight;
            this.goalType = goalType;
            this.rawTarget = rawTarget;
            this.rawTimeframe = rawTimeframe;
        }

        static GoalsValidationResult redirect(final String message) {
            return new GoalsValidationResult(
                    false, true, message,
                    0, 0, 0,
                    0, 0.0,
                    null, null, null
            );
        }

        static GoalsValidationResult fail(final String message) {
            return new GoalsValidationResult(
                    false, false, message,
                    0, 0, 0,
                    0, 0.0,
                    null, null, null
            );
        }

        static GoalsValidationResult success(final int currentWeight,
                                             final int age,
                                             final int height,
                                             final int timeframe,
                                             final double targetWeight,
                                             final String goalType,
                                             final String rawTarget,
                                             final String rawTimeframe) {
            return new GoalsValidationResult(
                    true, false, null,
                    currentWeight, age, height,
                    timeframe, targetWeight,
                    goalType,
                    rawTarget,
                    rawTimeframe
            );
        }

        boolean isValid() {
            return valid;
        }

        boolean isRedirectToSettings() {
            return redirectToSettings;
        }

        String getErrorMessage() {
            return errorMessage;
        }

        int getCurrentWeight() {
            return currentWeight;
        }

        int getAge() {
            return age;
        }

        int getHeight() {
            return height;
        }

        int getTimeframe() {
            return timeframe;
        }

        double getTargetWeight() {
            return targetWeight;
        }

        String getGoalType() {
            return goalType;
        }

        String getRawTarget() {
            return rawTarget;
        }

        String getRawTimeframe() {
            return rawTimeframe;
        }
    }
}
