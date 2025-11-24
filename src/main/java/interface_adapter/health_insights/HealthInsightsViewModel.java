package interface_adapter.health_insights;

public class HealthInsightsViewModel {
    private HealthInsightsState state;
    private final String viewName;

    public HealthInsightsViewModel() {
        this.viewName = "health insights";
        this.state = new HealthInsightsState();
    }

    public HealthInsightsState getState() {
        return state;
    }

    public void setState(HealthInsightsState state) {
        this.state = state;
    }

    public String getViewName() {
        return viewName;
    }

    public void addPropertyChangeListener(Object listener) {
    }
}
