package use_case.healthHistory;
/**
 * The input data for the View Health Score History Use Case.
 */
public class healthHistoryInputData {
    private final String metricType;   // (steps, water, calories, etc.)
    private final String timeRange;// (day, week, month, year, etc.))
    private final String user;

    public healthHistoryInputData(String metricType, String timeRange, String user) {
        this.metricType = metricType;
        this.timeRange = timeRange;
        this.user = user;
    }

    public String getMetricType() {
        return metricType;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public String getUser(){return user;}

}
