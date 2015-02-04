package de.lucaspradel.mydart.game.x01;

/**
 * Created by lpradel on 1/24/15.
 */
public class GamePlayer {

    private final int id;
    private String name;
    private int score;
    private int legs;
    private int sets;
    private int scoringRound = 1;

    public GamePlayer(int score, int legs, int sets, String name, int id) {
        this.score = score;
        this.legs = legs;
        this.sets = sets;
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLegs() {
        return legs;
    }

    public void setLegs(int legs) {
        this.legs = legs;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }
    public int getId() {
        return id;
    }
    public int getScoringRound() {
        return scoringRound;
    }
    public void setScoringRound(int scoringRound) {
        this.scoringRound = scoringRound;
    }
}
