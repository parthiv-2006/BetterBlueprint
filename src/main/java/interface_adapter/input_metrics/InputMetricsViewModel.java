package interface_adapter.input_metrics;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Input Metrics View.
 */
public class InputMetricsViewModel extends ViewModel<InputMetricsState> {

    public static final String TITLE_LABEL = "Input Daily Health Metrics";
    public static final String SLEEP_LABEL = "Sleep (hours)";
    public static final String STEPS_LABEL = "Steps"; // ADDED this line
    public static final String WATER_LABEL = "Water Intake (liters)";
    public static final String CALORIES_LABEL = "Calories Consumed";
    public static final String EXERCISE_LABEL = "Exercise Duration (minutes)";

    public static final String SUBMIT_BUTTON_LABEL = "Submit Metrics";
    public static final String BACK_BUTTON_LABEL = "Back to Home";

    public InputMetricsViewModel() {
        super("input metrics");
        setState(new InputMetricsState());
    }

    @Override
    public void reset() {
        setState(new InputMetricsState());
        firePropertyChange();
    }
}