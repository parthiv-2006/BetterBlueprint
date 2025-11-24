package use_case.input_metrics;

import java.time.LocalDate;

/**
 * Output data for the Input Metrics use case.
 */
public class InputMetricsOutputData {
    private final String username;
    private final LocalDate date;
    private final boolean success;

    public InputMetricsOutputData(String username, LocalDate date, boolean success) {
        this.username = username;
        this.date = date;
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isSuccess() {
        return success;
    }
}
