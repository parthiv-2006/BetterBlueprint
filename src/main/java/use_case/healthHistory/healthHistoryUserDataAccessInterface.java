package use_case.healthHistory;

import Entities.HealthMetrics;
import java.util.List;

public interface healthHistoryUserDataAccessInterface {
    List<HealthMetrics> getHealthMetricsByUser(String userId);

}
