package interface_adapter.daily_health_score;

import interface_adapter.ViewManagerModel;
import interface_adapter.signup.SignupViewModel;
import use_case.daily_health_score.DailyHealthScoreOutputBoundary;
import use_case.daily_health_score.DailyHealthScoreOutputData;

/** -------------------------------------------------------------------
 * A Presenter class receives information (an Output Data Object) from the
 * Use Case Interactor and turns it into raw strings and numbers to be displayed.
 * The presenter will update the View Model with this information.
 * --------------------------------------------------------------------
 */

public class DailyHealthScorePresenter implements DailyHealthScoreOutputBoundary {

    // write private final viewModels below
    private final DailyHealthScoreViewModel dailyHealthScoreViewModel;
    private final ViewManagerModel viewManagerModel;

    // constructor. viewModels as parameters & this.viewModel = viewModel for each
    public DailyHealthScorePresenter(ViewManagerModel viewManagerModel, DailyHealthScoreViewModel dailyHealthScoreViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.dailyHealthScoreViewModel = dailyHealthScoreViewModel;
    }

    @Override
    public void prepareSuccessView(DailyHealthScoreOutputData outputData) {
        DailyHealthScoreState state = dailyHealthScoreViewModel.getState();

        state.setScore(outputData.getHealthScore());
        state.setSummaryFeedback(outputData.getSummary());
        state.setDate(outputData.getDate());
        state.setErrorMessage(null);

        dailyHealthScoreViewModel.setState(state);
        dailyHealthScoreViewModel.firePropertyChanged();

        viewManagerModel.setActiveView(dailyHealthScoreViewModel.getViewName());
        viewManagerModel.firePropertyChanged();


    }

    @Override
    public void prepareFailView(String errorMessage) {
        DailyHealthScoreState state = dailyHealthScoreViewModel.getState();

        state.setErrorMessage(errorMessage);

        dailyHealthScoreViewModel.setState(state);
        dailyHealthScoreViewModel.firePropertyChanged();

        viewManagerModel.setActiveView(dailyHealthScoreViewModel.getViewName());
        viewManagerModel.firePropertyChanged();

    }
}
