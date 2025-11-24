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

    // write private final viewModels below

    // constructor. viewModels as parameters & this.viewModel = viewModel for each
    public DailyHealthScorePresenter() {

    }

    @Override
    public void prepareSuccessView(DailyHealthScoreOutputData outputData) {
        // on success, do ...   likely will deal with STATES


    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}
