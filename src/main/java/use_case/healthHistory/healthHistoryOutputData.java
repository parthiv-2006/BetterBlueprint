package use_case.healthHistory;

import java.util.List;

/**
 * Output data for the Health Score History use case.
 */
public class healthHistoryOutputData {

    private final String metricType;
    private final String timeRange;
    private final List<healthMetricRecord> records;

    public healthHistoryOutputData(String timeRange, String metricType, List<healthMetricRecord> records) {
        this.records = records;
        this.metricType = metricType;
        this.timeRange = timeRange;
    }

    public String getMetricType() {
        return metricType;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public List<healthMetricRecord> getRecords() {
        return records;
    }
}
