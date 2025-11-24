package view;

import interface_adapter.health_history.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The View for the Health History use case.
 */
public class HealthHistoryView extends JPanel {

    private List<String> dates;
    private List<Double> values;
    private String metricType;

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
        int bottomPadding = 60; // increased for date labels

        int graphWidth = width - padding * 2 - labelPadding;
        int graphHeight = height - padding - bottomPadding;

        double maxValue = values.stream().mapToDouble(v -> v).max().orElse(1);
        double minValue = values.stream().mapToDouble(v -> v).min().orElse(0);

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

        for (int i = 0; i < dates.size(); i++) {
            int x = padding + labelPadding + (i * graphWidth / (dates.size() - 1));
            g2.drawLine(x, padding, x, padding + graphHeight);
        }

        // Draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(padding + labelPadding, padding, padding + labelPadding, padding + graphHeight);
        g2.drawLine(padding + labelPadding, padding + graphHeight,
                padding + labelPadding + graphWidth, padding + graphHeight);

        // Draw axis labels
        g2.drawString(metricType, padding, padding - 10);
        g2.drawString("Date", width / 2 - 20, height - 10);

        // Draw date labels (X-axis)
        for (int i = 0; i < dates.size(); i++) {
            int x = padding + labelPadding + (i * graphWidth / (dates.size() - 1));
            g2.drawString(dates.get(i), x - 20, padding + graphHeight + 20);
        }

        // Draw values (Y-axis labels)
        for (int i = 0; i <= gridCount; i++) {
            double val = maxValue - (maxValue - minValue) * i / gridCount;
            int y = padding + (i * graphHeight / gridCount);
            g2.drawString(String.format("%.1f", val), padding, y + 5);
        }

        // Draw data points and connecting lines
        g2.setColor(Color.BLUE);

        int prevX = -1;
        int prevY = -1;

        for (int i = 0; i < values.size(); i++) {
            double value = values.get(i);

            int x = padding + labelPadding + (i * graphWidth / (values.size() - 1));
            int y = padding + (int) ((maxValue - value) / (maxValue - minValue) * graphHeight);

            g2.fillOval(x - 3, y - 3, 6, 6);

            if (i > 0) {
                g2.drawLine(prevX, prevY, x, y);
            }

            prevX = x;
            prevY = y;
        }
    }

    private void drawEmptyMessage(Graphics g) {
        g.drawString("No data available for this range.", getWidth() / 2 - 50, getHeight() / 2);
    }
}
