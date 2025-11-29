package interface_adapter.daily_health_score;

import interface_adapter.ViewModel;

/**
 * The View Model for the Daily Health Score Use Case.
 */

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DailyHealthScoreViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private DailyHealthScoreState state = new DailyHealthScoreState();

    public DailyHealthScoreState getState() {
        return state;
    }

    public void setState(DailyHealthScoreState newState) {
        this.state = newState;
    }

    public void firePropertyChanged() {
        support.firePropertyChange(
                "dailyHealthScoreState",
                null,     // old value not needed for this use case
                state     // send the updated state
        );
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
