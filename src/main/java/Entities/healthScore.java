package Entities;

/**
 * A simple entity representing a healthScore.java. An algorithm looks at user's history and
 * returns an overall score (out of 100)
 */
public class healthScore{
    private int score;

    /**
     * Creates a new score with the given non-empty int.
     * @param score: number that represents the healthScore.java of user
     */

    //Constructor to make a new score object
    public healthScore(int score){
        this.score = score;
    }

    //getter method
    public int get_score(){
        return score;
    }

    //think about what the calculate score method should look like?
}

