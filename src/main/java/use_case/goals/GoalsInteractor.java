package use_case.goals;
import Entities.User;
import use_case.goals.GoalsInputBoundary;
import use_case.goals.GoalsInputData;
import use_case.goals.GoalsOutputBoundary;
import use_case.goals.GoalsOutputData;

public class GoalsInteractor implements GoalsInputBoundary {
    private final GoalsOutputBoundary presenter;
    private final User currentUser;  // Pass this via constructor

    public GoalsInteractor(GoalsOutputBoundary presenter, User currentUser) {
        this.presenter = presenter;
        this.currentUser = currentUser;
    }

    @Override
    public void execute(GoalsInputData inputData) {
        int currentWeight = currentUser.getWeight();
        int age = currentUser.getAge();
        int timeframe = Integer.parseInt(inputData.getTimeframe());
        String goalType = inputData.getGoalType();

        // Calculate BMR (Basal Metabolic Rate) using Mifflin-St Jeor equation
        // Simplified version (adjust for gender if needed)
        double bmr = 10 * currentWeight + 6.25 * currentUser.getHeight() - 5 * age + 5;
        double dailyBurn = bmr * 1.5;  // Assume moderate activity level

        // Calculate weekly weight change needed
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
