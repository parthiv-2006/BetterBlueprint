package interface_adapter.input_metrics;

import use_case.input_metrics.InputMetricsInputBoundary;
import use_case.input_metrics.InputMetricsInputData;

public class InputMetricsController {
    private final InputMetricsInputBoundary inputMetricsUseCase;

    public InputMetricsController(InputMetricsInputBoundary inputMetricsUseCase) {
        this.inputMetricsUseCase = inputMetricsUseCase;
    }

    public void execute(String username, float sleepHours, int steps, float waterIntake,
                        int calories, float exerciseDuration) {
        InputMetricsInputData inputData = new InputMetricsInputData(
                username, sleepHours, steps, waterIntake, calories, exerciseDuration
        );
        inputMetricsUseCase.execute(inputData);
    }
}