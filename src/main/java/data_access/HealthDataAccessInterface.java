package data_access;

import Entities.HealthMetrics;
import java.util.List;

public interface HealthDataAccessInterface {
    List<HealthMetrics> getHealthMetricsByUser(String userId);
    void saveHealthMetrics(HealthMetrics healthMetrics);
}
