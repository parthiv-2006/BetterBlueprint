package interface_adapter.daily_health_score;

import interface_adapter.ViewModel;

/** ---------------------------------------------------------------------
 * This is a storage class for information the View needs to display.
 * Does the View display the currently logged-in user's name?
 * Then the View Model stores that name. The View accesses the View Model
 * for any information it needs.
 * ----------------------------------------------------------------------
 */

public class DailyHealthScoreViewModel extends ViewModel<DailyHealthScoreState> {
    public DailyHealthScoreViewModel() {
        super("daily health score");
        setState(new DailyHealthScoreState());
    }
}