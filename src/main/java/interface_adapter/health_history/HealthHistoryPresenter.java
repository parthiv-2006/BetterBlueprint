package interface_adapter.health_history;
import use_case.healthHistory.healthHistoryOutputBoundary;
import use_case.healthHistory.healthHistoryOutputData;
import java.util.List;
import java.util.ArrayList;
import use_case.healthHistory.healthMetricRecord;

/**
 * The presenter for the Health History Use Case.
 */

public class HealthHistoryPresenter implements healthHistoryOutputBoundary {

    private final HealthHistoryViewModel viewModel;

    public HealthHistoryPresenter(HealthHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(healthHistoryOutputData data) {

        List<String> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        List<healthMetricRecord> records = data.getRecords();

        for (healthMetricRecord record : records) {
            dates.add(record.getDate().toString());
            values.add(record.getValue());
        }

        HealthHistoryState state = viewModel.getState();
        state.setMetricType(data.getMetricType());
        state.setTimeRange(data.getTimeRange());
        state.setDates(dates);
        state.setValues(values);
        state.setErrorMessage(null);

        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        HealthHistoryState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();
    }
}