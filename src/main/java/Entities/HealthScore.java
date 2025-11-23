package Entities;

import java.time.LocalDate;

/**
 * A simple entity representing a healthScore.java. An algorithm looks at user's history and
 * returns an overall score (out of 100)
 */
public class HealthScore {
    private int score;
    private LocalDate date;

    /**
     * Creates a new score with the given non-empty int.
     *
     * @param score: number that represents the healthScore.java of user
     */

    //Constructor to make a new score object
    public HealthScore(int score, LocalDate date) {
        this.score = score;
        this.date = date;
    }

    //getter method
    public int get_score() {
        return score;
    }

    public LocalDate getDate() {
        return date;
    }

}
