package interface_adapter.health_insights;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class HealthInsightsViewModel extends ViewModel<HealthInsightsState> {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final String viewName;

    public HealthInsightsViewModel() {
        super("health insights");
        this.viewName = "health insights";
        this.setState(new HealthInsightsState());
    }

    @Override
    public void firePropertyChange() {
        support.firePropertyChange("state", null, this.getState());
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public HealthInsightsState getState() {
        return super.getState();
    }

    public void setState(HealthInsightsState state) {
        super.setState(state);
    }

    public String getViewName() {
        return viewName;
    }
}