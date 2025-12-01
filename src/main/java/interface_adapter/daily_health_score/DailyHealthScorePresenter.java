package interface_adapter.daily_health_score;

import use_case.daily_health_score.DailyHealthScoreOutputBoundary;
import use_case.daily_health_score.DailyHealthScoreOutputData;
import Entities.HealthMetrics;

/**
 * A Presenter class for the Daily Health Score Use Case.
 */

public class DailyHealthScorePresenter implements DailyHealthScoreOutputBoundary {

    private final DailyHealthScoreViewModel viewModel;

    public DailyHealthScorePresenter(DailyHealthScoreViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(DailyHealthScoreOutputData data) {
        DailyHealthScoreState newState = new DailyHealthScoreState();

        newState.setUserId(data.getUserId());
        newState.setDate(data.getDate());
        newState.setScore(data.getScore());
        newState.setFeedback(data.getFeedback());
        newState.setErrorMessage(null);

        HealthMetrics m = data.getMetrics();
        newState.setSleepHours(m.getSleepHours());
        newState.setExerciseMinutes(m.getExerciseMinutes());
        newState.setCalories(m.getCalories());
        newState.setWaterIntake(m.getWaterIntake());
        newState.setSteps(m.getSteps());

        // Tell the view model to update and notify listeners
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        DailyHealthScoreState newState = new DailyHealthScoreState();

        newState.setErrorMessage(errorMessage);
        newState.setDate(null);
        newState.setScore(null);
        newState.setFeedback("");

        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }
}
