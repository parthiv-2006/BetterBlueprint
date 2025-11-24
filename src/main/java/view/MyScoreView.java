package view;

import interface_adapter.daily_health_score.DailyHealthScoreViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for the Daily Health Score use case (MyScore page)
 */

public class MyScoreView extends JPanel implements ActionListener, PropertyChangeListener {
    // variables
    private DailyHealthScoreViewModel dailyHealthScoreViewModel;

    // constructor
    public MyScoreView(DailyHealthScoreViewModel dailyHealthScoreViewModel) {
        this.dailyHealthScoreViewModel = dailyHealthScoreViewModel;
        dailyHealthScoreViewModel.addPropertyChangeListener(this);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
