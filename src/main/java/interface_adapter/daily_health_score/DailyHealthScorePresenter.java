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
    public void prepareSuccessView(DailyHealthScoreOutputData outputData) {
        DailyHealthScoreState newState = new DailyHealthScoreState();

        newState.setUserId(outputData.getUserId());
        newState.setDate(outputData.getDate());
        newState.setScore(outputData.getScore());
        newState.setFeedback(outputData.getFeedback());
        newState.setErrorMessage(null);

        viewModel.setState(newState);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        DailyHealthScoreState newState = new DailyHealthScoreState();
        newState.setErrorMessage(errorMessage);

        // Clear prior results
        newState.setDate(null);
        newState.setScore(null);
        newState.setFeedback("");

        viewModel.setState(newState);
    }
}
