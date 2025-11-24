package interface_adapter.daily_health_score;

import use_case.daily_health_score.DailyHealthScoreOutputBoundary;
import use_case.daily_health_score.DailyHealthScoreOutputData;

/** -------------------------------------------------------------------
 * A Presenter class receives information (an Output Data Object) from the
 * Use Case Interactor and turns it into raw strings and numbers to be displayed.
 * The presenter will update the View Model with this information.
 * --------------------------------------------------------------------
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

