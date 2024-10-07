package model;

import view.GameCanvas;

import java.io.Serializable;

/**
 * The type Player.
 */
public class Player implements Serializable {
    private String name;
    private int score;
    private int number;
    private Racket racket;

    private Game game;

    /**
     * Instantiates a new Player.
     * This player object will have its own:
     * Number (To tell player 1 and 2 apart)
     * Name: Set to player 1 by default by can be changed
     * Racket: Needed for the player to control the game
     * Score: Keeps track of how many points a player has won
     *
     * @param number the number
     * @param name   the name
     * @param racket the racket
     * @param game   the game
     */
    public Player(int number, String name, Racket racket, Game game) {
        this.number = number;
        this.name = name;
        this.score = 0;
        this.racket = racket;
        this.game = game;
    }

    /**
     * Gets number.
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets racket.
     *
     * @return the racket
     */
    public Racket getRacket() {
        return racket;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets game.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets game.
     *
     * @param game the game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets score.
     *
     * @param score the score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Increase score.
     */
    public void increaseScore() {

        this.score++;
    }
}
