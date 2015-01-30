package de.lucaspradel.mydart.game.x01;

/**
 * Created by lpradel on 1/24/15.
 */
public class GamePlayer {

    private String name;
    private int score;
    private int legs;
    private int sets;

    public GamePlayer(int score, int legs, int sets, String name) {
        this.score = score;
        this.legs = legs;
        this.sets = sets;
        this.name = name;
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
}
