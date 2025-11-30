package use_case.goals;
import Entities.User;
import data_access.UserDataAccessInterface;
import use_case.goals.GoalsInputBoundary;
import use_case.goals.GoalsInputData;
import use_case.goals.GoalsOutputBoundary;
import use_case.goals.GoalsOutputData;

public class GoalsInteractor implements GoalsInputBoundary {
    private final GoalsOutputBoundary presenter;
    private final UserDataAccessInterface userDataAccess;  // Use DAO to fetch current user

    public GoalsInteractor(GoalsOutputBoundary presenter, UserDataAccessInterface userDataAccess) {
        this.presenter = presenter;
        this.userDataAccess = userDataAccess;
    }

    @Override
    public void execute(GoalsInputData inputData) {
        // Fetch currently active username and user entity via DAO
        String currentUsername = userDataAccess.getCurrentUsername();
        if (currentUsername == null) {
            presenter.redirectToSettings("Please set your weight in Settings before using Goals.");
            return;
        }

        User currentUser = userDataAccess.get(currentUsername);
        if (currentUser == null || currentUser.getWeight() <= 0) {
            presenter.redirectToSettings("Please set your weight in Settings before using Goals.");
            return;
        }

        int currentWeight = currentUser.getWeight();
        int age = currentUser.getAge();
        int timeframe = 1;
        try {
            timeframe = inputData.getTimeframe() == null || inputData.getTimeframe().isEmpty() ? 1 : Integer.parseInt(inputData.getTimeframe());
            if (timeframe <= 0) timeframe = 1;
        } catch (NumberFormatException ex) {
            timeframe = 1;
        }
        String goalType = inputData.getGoalType();

        // Calculate BMR (Basal Metabolic Rate) using Mifflin-St Jeor equation
        double bmr = 10 * currentWeight + 6.25 * currentUser.getHeight() - 5 * age + 5;
        double dailyBurn = bmr * 1.5;  // Assume moderate activity level

        double targetWeight = inputData.getTarget().isEmpty() ? currentWeight : Double.parseDouble(inputData.getTarget());
        double weeklyWeightChange = (targetWeight - currentWeight) / timeframe;

        // 1 kg = ~7700 calories
        double dailyCalorieAdjustment = (weeklyWeightChange * 7700) / 7;
        double dailyIntake = dailyBurn + dailyCalorieAdjustment;

        String explanation = generateExplanation(goalType, dailyIntake, dailyBurn);

        GoalsOutputData outputData = new GoalsOutputData(
                String.format("%.0f", dailyIntake),
                String.format("%.0f", dailyBurn),
                explanation
        );

        presenter.present(outputData);
    }

    private String generateExplanation(String goalType, double intake, double burn) {
        if ("Weight Loss".equals(goalType)) {
            return "To lose weight, maintain a caloric deficit by consuming less than your daily burn.";
        } else if ("Weight Gain".equals(goalType)) {
            return "To gain weight, maintain a caloric surplus by consuming more than your daily burn.";
        }
        return "Maintain your current weight by consuming approximately your daily burn calories.";
    }
}
