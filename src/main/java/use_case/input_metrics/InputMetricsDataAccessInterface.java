package use_case.input_metrics;

import Entities.HealthMetrics;

/**
 * Data access interface for the Input Metrics use case.
 */
public interface InputMetricsDataAccessInterface {
    /**
     * Saves the health metrics.
     * @param healthMetrics the health metrics to save
     */
    void saveHealthMetrics(HealthMetrics healthMetrics);

    /**
     * Gets the current logged-in username.
     * @return the current username
     */
    String getCurrentUsername();
}

