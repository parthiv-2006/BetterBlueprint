package interface_adapter.goals;

import interface_adapter.ViewModel;

/**
 * ViewModel for the Goals UI. Observable by GoalsView(s).
 */
public class GoalsViewModel extends ViewModel<GoalsState> {

    public static final String VIEW_NAME = "goals";

    public GoalsViewModel() {
        super("Goals");
        setState(new GoalsState());
    }
}