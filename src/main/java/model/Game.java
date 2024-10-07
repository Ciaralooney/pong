package model;

import controller.BallManager;
import view.GameCanvas;

import java.io.Serializable;

/**
 * The type Game.
 */
public class Game implements Serializable {
    private Racket racket1;
    private Racket racket2;
    private Player player1;
    private Player player2;
    private Ball ball;
    private BallManager ballManager;
    private int limit = 5; // default limit
    private GameCanvas gameCanvas;

    /**
     * Instantiates a new Game.
     * This creates a new instance of most of the classes needed for the game
     * A racket is made for each player and assigned to it
     * Players are created and given a player number and racket
     * A new ball and ball manager are created too
     *
     * @param gameCanvas the game canvas
     */
    public Game(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
        this.racket1 = new Racket(50, 100, 20, 90, this);
        this.racket2 = new Racket(550, 100, 20, 90, this);
        this.player1 = new Player(1, "Player 1", racket1, this);
        this.player2 = new Player(2, "Player 2", racket2, this);
        this.ball = new Ball(this, 100, 100, 10, 5, 10, 2);
        this.ballManager = new BallManager(this, gameCanvas);
    }

    /**
     * Gets game canvas.
     *
     * @return the game canvas
     */
    public GameCanvas getGameCanvas() {
        return gameCanvas;
    }

    /**
     * Checking if either players score is more than the limit.
     *
     * @return the boolean
     */
    public boolean gameWon() {
        return player1.getScore() >= limit || player2.getScore() >= limit;
    }

    /**
     * When the game is over it finds the winning player and passes it to the end screen
     *
     * @return winningPlayer the number of the player who won
     */
    public int endGame() {
        // finding the winner
        int winningPlayer;
        if (getPlayer(1).getScore() >= getLimit()) {
            winningPlayer = 1;
        } else {
            winningPlayer = 2;
        }


        ballManager.pauseBall();
        gameCanvas.createGameOverScreen(winningPlayer);
        return winningPlayer;
    }

    /**
     *   Reset the game to default values
     */
    public void resetGame() {
        setLimit(5);
        player1.setScore(0);
        player2.setScore(0);

        // setting ball to default position
        ball.setX(gameCanvas.getWidth() / 2);
        ball.setY(gameCanvas.getHeight() / 2);

        ball.setSize(10);
        ball.setSpeedX(5);
        ball.setSpeedY(10);
        ball.setTimer(2);
        player1.setName("Player 1");
        player2.setName("Player 2");

        getPlayer(1).getRacket().setPosX(50);
        getPlayer(1).getRacket().setPosY(100);
        getPlayer(2).getRacket().setPosX(550);
        getPlayer(2).getRacket().setPosY(100);
        getPlayer(1).getRacket().setWidth(20);
        getPlayer(1).getRacket().setHeight(90);
        getPlayer(2).getRacket().setWidth(20);
        getPlayer(2).getRacket().setHeight(90);

        // unpause the ball
        ballManager.resumeBall();


    }

    /**
     * Gets ball manager.
     *
     * @return the ball manager
     */
    public BallManager getBallManager() {
        return ballManager;
    }

    /**
     * Sets ball.
     *
     * @param ball the ball
     */
    public void setBall(Ball ball) {
        this.ball = ball;
    }

    /**
     * Gets limit.
     *
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Finds the specified player by using the player number that has been passed through
     *
     * @param playerNumber the player number
     * @return the player
     */
    public Player getPlayer(int playerNumber) {
        if (player1.getNumber() == playerNumber) {
            return player1;
        } else if (player2.getNumber() == playerNumber) {
            return player2;
        } else {
            return null;
        }
    }

    /**
     * Setting a game limit.
     * The player score cannot go above this.
     *
     * @param limit the limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Gets ball.
     *
     * @return the ball
     */
    public Ball getBall() {
        return ball;
    }

}
