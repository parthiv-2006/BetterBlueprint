package view;

import interface_adapter.health_history.*;
import use_case.healthHistory.healthHistoryInteractor;
import use_case.healthHistory.healthHistoryOutputBoundary;
import use_case.healthHistory.healthHistoryOutputData;
import use_case.healthHistory.healthMetricRecord;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * The View for the Health History use case.
 */
public class HealthHistoryView extends JPanel {
    private final String viewName = "health history";
    private List<String> dates;
    private List<Double> values;
    private String metricType;

    public HealthHistoryView(){
        setBackground(Color.WHITE);
    }

    /**
     * Attach a HealthHistoryViewModel so the view automatically updates when the presenter
     * publishes new state. The ViewModel is expected to provide:
     *  - getState() -> HealthHistoryState
     *  - addPropertyChangeListener(PropertyChangeListener)
     */
    public void setViewModel(HealthHistoryViewModel vm) {
        if (vm == null) return;

        // Register a listener that pulls the state and updates the chart
        try {
            vm.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    HealthHistoryState state = vm.getState();
                    if (state != null && state.getDates() != null && state.getValues() != null) {
                        updateData(state.getDates(), state.getValues(), state.getMetricType());
                    }
                }
            });

            // If ViewModel already has data, pull initial state
            HealthHistoryState initial = vm.getState();
            if (initial != null && initial.getDates() != null && initial.getValues() != null) {
                updateData(initial.getDates(), initial.getValues(), initial.getMetricType());
            }
        } catch (Exception e) {
            System.err.println("HealthHistoryView: failed to attach ViewModel: " + e.getMessage());
        }
    }


    public void updateData(List<String> dates, List<Double> values, String metricType) {
        this.dates = dates;
        this.values = values;
        this.metricType = metricType;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (dates == null || values == null || dates.isEmpty()) {
            drawEmptyMessage(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int padding = 50;
        int labelPadding = 40;
        int bottomPadding = 60;

        int graphWidth = width - padding * 2 - labelPadding;
        int graphHeight = height - padding - bottomPadding;

        double maxValue = values.stream().mapToDouble(v -> v).max().orElse(1);
        double minValue = values.stream().mapToDouble(v -> v).min().orElse(0);

        double range = maxValue - minValue;
        if (range == 0) {
            range = Math.max(1.0, Math.abs(maxValue) * 0.1);
            minValue = maxValue - range;
        }

        // Draw background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        // Draw grid lines
        g2.setColor(new Color(200, 200, 200));

        int gridCount = 10;
        for (int i = 0; i <= gridCount; i++) {
            int y = padding + (i * graphHeight / gridCount);
            g2.drawLine(padding + labelPadding, y, padding + labelPadding + graphWidth, y);
        }

        // Avoid division by zero if only one date (places the single vertical guide at center)
        int stepsX = Math.max(1, dates.size() - 1);
        for (int i = 0; i < dates.size(); i++) {
            int x = padding + labelPadding + (i * graphWidth / stepsX);
            g2.drawLine(x, padding, x, padding + graphHeight);
        }

        // Draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(padding + labelPadding, padding, padding + labelPadding, padding + graphHeight);
        g2.drawLine(padding + labelPadding, padding + graphHeight,
                padding + labelPadding + graphWidth, padding + graphHeight);

        String title = humanMetricLabel(metricType);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2.drawString(title, Math.max(padding + labelPadding, (width - titleWidth) / 2), padding - 18);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        // Draw date labels (X-axis)
        for (int i = 0; i < dates.size(); i++) {
            int x = padding + labelPadding + (i * graphWidth / stepsX);
            int label = i + 1;
            int labelX = Math.max(padding + labelPadding, Math.min(x - 20, width - 60));
            g2.drawString(String.valueOf(label), labelX, padding + graphHeight + 20);
        }

        g2.drawString("Days", width / 2 - 15, height - 10);

        // Draw values (Y-axis labels)
        for (int i = 0; i <= gridCount; i++) {
            double val = maxValue - (range) * i / gridCount;
            int y = padding + (i * graphHeight / gridCount);
            String yLabel = formatYValue(val, metricType);
            g2.drawString(yLabel, padding, y + 5);
        }

        // Draw data points and connecting lines
        g2.setColor(new Color(33, 150, 243)); // blue-ish
        int prevX = -1;
        int prevY = -1;

        for (int i = 0; i < values.size(); i++) {
            double value = values.get(i);

            int x = padding + labelPadding + (i * graphWidth / stepsX);
            int y = padding + (int) ((maxValue - value) / (range) * graphHeight);

            g2.fillOval(x - 4, y - 4, 8, 8);

            if (i > 0 && prevX >= 0) {
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(prevX, prevY, x, y);
            }

            prevX = x;
            prevY = y;
        }
    }

    private String humanMetricLabel(String metric) {
        if (metric == null) return "";
        switch (metric.toLowerCase()) {
            case "sleephours": return "Sleep (Hours)";
            case "waterintake": return "Water Intake (Litres)";
            case "exerciseminutes": return "Exercise (Minutes)";
            case "calories": return "Calories (cal)";
            default: return metric;
        }
    }

    // Format Y-axis values with suitable precision / units
    private String formatYValue(double v, String metric) {
        if (metric == null) return String.format("%.1f", v);
        switch (metric.toLowerCase()) {
            default:
                return String.format("%.1f", v);
        }
    }

    private void drawEmptyMessage(Graphics g) {
        String msg = "No data available for this range.";
        FontMetrics fm = g.getFontMetrics();
        int msgWidth = fm.stringWidth(msg);
        int x = Math.max(0, (getWidth() - msgWidth) / 2);
        int y = getHeight() / 2;
        g.drawString(msg, x, y);
    }

    public static healthHistoryInteractor getHealthHistoryInteractor(HealthHistoryView historyView) {
        healthHistoryOutputBoundary directPresenter = new healthHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(healthHistoryOutputData data) {
                List<String> formatted = new ArrayList<>();
                List<Double> values = new ArrayList<>();
                List<healthMetricRecord> records = data.getRecords();
                DateTimeFormatter iso = DateTimeFormatter.ISO_LOCAL_DATE;
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");

                if (records != null) {
                    for (healthMetricRecord r : records) {
                        String rawDate = r.getDate().toString();
                        try {
                            formatted.add(java.time.LocalDate.parse(rawDate, iso).format(fmt));
                        } catch (Exception ex) {
                            formatted.add(rawDate);
                        }
                        values.add(r.getValue());
                    }
                }

                historyView.updateData(formatted, values, data.getMetricType());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                historyView.updateData(new ArrayList<>(), new ArrayList<>(), "");
            }
        };

        final healthHistoryInteractor historyInteractor = new healthHistoryInteractor(null, directPresenter);
        return historyInteractor;
    }
}
