package use_case.input_metrics;

public class InputMetricsOutputData {
    private final String date;
    private final String message;
    private final boolean success;

    public InputMetricsOutputData(String date, String message, boolean success) {
        this.date = date;
        this.message = message;
        this.success = success;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}