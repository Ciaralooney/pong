package controller;

import model.Ball;
import model.Game;
import view.GameCanvas;
import javafx.application.Platform;

import java.io.Serializable;

/**
 * Manages the ball movement and collisions with rackets and boundaries,
 * It manages the game state when goals are scored.
 */
public class BallManager implements Runnable, Serializable {
    private Ball ball;
    private GameCanvas gameCanvas;
    private Game game;
    private boolean running;
    private boolean paused = false;

    /**
     * Instantiates a new Ball manager.
     *
     * @param game       the game instance
     * @param gameCanvas the game canvas
     */
    public BallManager(Game game, GameCanvas gameCanvas) {
        this.game = game;
        this.gameCanvas = gameCanvas;
        this.ball = game.getBall();
        this.running = true;

        this.paused = false;

}

    /**
     * This will run while the ball isn't paused.
     * In this case it moves the ball, checks for collisions and will redraw the canvas
     */
    @Override
    public void run() {
        while (running) {
            if (!paused) {
                moveBall();
                checkRacketCollisions();
                checkBoundsCollisions();
                Platform.runLater(() -> gameCanvas.redraw(game));
            }
            try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

    /**
     * ball paused boolean.
     *
     * @return the boolean
     */
    public boolean isPaused() {
        return paused;
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
     * Gets game.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Checks for collisions between the ball top/bottom side of the screen
     * Handles goal scoring when the ball goes behind a racket
     */
    private void checkBoundsCollisions() {
        Ball ball = game.getBall();
        if (ball.getX() <= 0) {
            handleGoal(); // player 1
        } else if (ball.getX() + ball.getSize() >= game.getGameCanvas().getWidth()) {
            handleGoal(); // player 2
        }

        // top and bottom wall collisions
        if (ball.getY() <= 0) {
            ball.setY(1); // moving ball slightly below the top wall
        } else if (ball.getY() + ball.getSize() >= game.getGameCanvas().getHeight()) {
            ball.setY(game.getGameCanvas().getHeight() - ball.getSize() - 1);
        }
    }

    /**
     * Pause ball movement.
     */
    public void pauseBall() {
        paused = true;
        game.getBall().pause();
    }

    /**
     * Resume ball movement.
     */
    public void resumeBall() {
        paused = false;
        game.getBall().resume();
    }

    /**
     * Moves ball if it is not paused.
     */
    public void moveBall() {
        if (!paused) {
            ball.move();
        }
    }

    /**
     * Handles goal scoring.
     * Increases player score.
     * Displays a message saying which player scored.
     * Checking if the game is won. If so ending the game
     */
    private void handleGoal() {
        Platform.runLater(() -> {
            if (ball.getX() <= 0) {    // When the ball hits the wall behind Player 2's racket
                game.getPlayer(2).increaseScore();
                game.getGameCanvas().showGoalMessage(2); //passing player number through

            } else if (ball.getX() + ball.getSize() >= game.getGameCanvas().getWidth()) {
                // When the ball hits the wall behind Player 1's racket
                game.getPlayer(1).increaseScore();
                game.getGameCanvas().showGoalMessage(1); //passing player number through
            }

            if (game.gameWon()) {
                ball.pauseBall();
                game.endGame();
            }

            resetBallPosition();
        });
    }

    /**
     * Checks for collisions between the ball and player rackets.
     */
    public void checkRacketCollisions() {
        if (game.getBall().collidesWithRacket(game.getPlayer(1).getRacket()) ||
                game.getBall().collidesWithRacket(game.getPlayer(2).getRacket())) {
            game.getBall().setSpeedX(-game.getBall().getSpeedX()); // reverse direction
        }
    }

    /**
     * Reset ball position.
     */
    public void resetBallPosition() {
        game.getBall().setX(game.getGameCanvas().getWidth() / 2 - game.getBall().getSize() / 2);
        game.getBall().setY(game.getGameCanvas().getHeight() / 2 - game.getBall().getSize() / 2);
    }

}
