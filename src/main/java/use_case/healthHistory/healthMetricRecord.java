package use_case.healthHistory;

import java.time.LocalDate;

/**
 * The data type for presenting healthHistory
 */

public class healthMetricRecord {

    private final LocalDate date;
    private final double value;

    public healthMetricRecord(LocalDate date, double value) {
        this.date = date;
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }
}
