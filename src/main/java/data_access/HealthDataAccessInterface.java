package data_access;

import Entities.HealthMetrics;
import java.util.List;

public interface HealthDataAccessInterface {
    void saveHealthMetrics(HealthMetrics healthMetrics);
    List<HealthMetrics> getHealthMetricsByUser(String userId);
    String getCurrentUsername();
}