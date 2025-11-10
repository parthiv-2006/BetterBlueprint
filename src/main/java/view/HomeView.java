package view;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JPanel {
    // Define buttons as class fields so controllers can access them
    public final JButton inputMetrics;
    public final JButton accountSettings;
    public final JButton myScore;
    public final JButton insights;
    public final JButton history;
    public final JButton goals;

    public HomeView() {
        // Use a layout manager (e.g., GridLayout) to arrange buttons
        this.setLayout(new GridLayout(6, 1)); // 6 rows, 1 column

        // Initialize buttons
        inputMetrics = new JButton("Input Health Metrics");
        accountSettings = new JButton("Account/Settings");
        myScore = new JButton("My Score");
        insights = new JButton("Insights");
        history = new JButton("History");
        goals = new JButton("Goals");

        // Add buttons to the panel
        this.add(inputMetrics);
        this.add(accountSettings);
        this.add(myScore);
        this.add(insights);
        this.add(history);
        this.add(goals);
    }
}