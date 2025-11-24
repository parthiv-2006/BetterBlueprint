package use_case.healthScoreHistory;

import Entities.HealthMetrics;
import java.util.List;

public interface healthScoreHistoryUserDataAccessInterface {
    List<HealthMetrics> getHealthMetricsByUser(String userId);

    String getCurrentUsername();
}
