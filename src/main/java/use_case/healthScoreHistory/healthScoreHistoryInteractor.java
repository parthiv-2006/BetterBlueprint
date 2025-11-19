package use_case.healthScoreHistory;

import Entities.HealthScore;
import java.util.List;

/**
 * The View Health Score History Interactor.
 */
public abstract class healthScoreHistoryInteractor implements healthScoreHistoryInputBoundary {

    private final healthScoreHistoryOutputBoundary presenter;

    public healthScoreHistoryInteractor(healthScoreHistoryOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(healthScoreHistoryInputData inputData) {

        if (inputData.getMetricType() == null || inputData.getMetricType().isEmpty()) {
            presenter.prepareFailView("Metric type cannot be empty.");
            return;
        }

        if (inputData.getTimeRange() == null || inputData.getTimeRange().isEmpty()) {
            presenter.prepareFailView("Time range cannot be empty.");
            return;
        }

        List<HealthMetricRecord> records = getHistory(
                inputData.getMetricType(),
                inputData.getTimeRange()
        );

        healthScoreHistoryOutputData outputData =
                new healthScoreHistoryOutputData(records, inputData.getMetricType(), inputData.getTimeRange());

        presenter.prepareSuccessView(outputData);
    }
}
