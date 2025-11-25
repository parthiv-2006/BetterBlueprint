package interface_adapter.health_history;
import interface_adapter.ViewModel;

/**
 * The View Model for the Health History View.
 */

public class HealthHistoryViewModel extends ViewModel<HealthHistoryState>{
    public HealthHistoryViewModel() {
        super("Health History");
        setState(new HealthHistoryState());
    }
}