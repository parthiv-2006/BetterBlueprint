package use_case.daily_health_score;


import Entities.HealthScore;

import java.time.LocalDate;

/**
 * Output Data for the Daily Health Score Use Case.
 */

public class DailyHealthScoreOutputData {
    private final int score;
    private final String summary;
    private final LocalDate date;

    public DailyHealthScoreOutputData(int score, String summary, LocalDate date) {
        this.score = score;
        this.summary = summary;
        this.date = date;
    }

    public int getScore() {return score;}

    public String getSummary() {return summary;}

    public LocalDate getDate() {return date;}
}