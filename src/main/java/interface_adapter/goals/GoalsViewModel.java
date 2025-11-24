package interface_adapter.goals;

import interface_adapter.ViewModel;

public class GoalsViewModel extends ViewModel<GoalsState> {

    public static final String VIEW_NAME = "goals";

    public GoalsViewModel() {
        super("Goals");
        setState(new GoalsState());
    }
}