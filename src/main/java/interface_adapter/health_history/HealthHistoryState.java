package interface_adapter.health_history;
import java.util.List;
/**
 * The state for the Health History View Model.
 */

public class HealthHistoryState {

    private String metricType;
    private String timeRange;
    private List<String> dates;
    private List<Double> values;
    private String errorMessage;

    //getters and setters
    public String getMetricType() { return metricType; }
    public void setMetricType(String metricType) { this.metricType = metricType; }

    public void setTimeRange(String timeRange) { this.timeRange = timeRange; }

    public List<String> getDates() { return dates; }
    public void setDates(List<String> dates) { this.dates = dates; }

    public List<Double> getValues() { return values; }
    public void setValues(List<Double> values) { this.values = values; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
