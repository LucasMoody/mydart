package de.lucaspradel.mydart.player;

/**
 * Created by lpradel on 1/16/15.
 */
public class Player {
    private String username;
    private int id;

    public Player(int id, String username){
        this.username = username;
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

}
