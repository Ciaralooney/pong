package model;

import view.GameCanvas;

import java.io.Serializable;

/**
 * The type Ball.
 */
public class Ball extends GameObject implements Serializable {
    private int timer;
    private double speedX;
    private double speedY;
    private double x;
    private double y;
    private double size;
    private Game game;
    private int bounces = 0;

    private final int maxBounces = 5; //default

    private boolean paused = false;

    /**
     * Instantiates a new Ball object.
     * There is only 1 ball object in this game.
     * This class encapsulates properties and behaviors of the ball.
     * Such as its position, size, speed, and game interaction.
     *
     * @param game          the game
     * @param x             the x position of ball
     * @param y             the y position of ball
     * @param size          the size of the ball
     * @param initialSpeedX the initial speed x of the ball
     * @param initialSpeedY the initial speed y of the ball
     * @param timer         the timer
     */
    public Ball(Game game, double x, double y, int size, double initialSpeedX, double initialSpeedY, int timer) {
        super(x, y);
        this.game = game;
        this.size = size;
        this.x = x;
        this.y = y;
        this.speedX = initialSpeedX;
        this.speedY = initialSpeedY;
        this.timer=timer;
    }

    /**
     * Gets timer.
     *
     * @return the timer
     */
    public int getTimer() {
        return timer;
    }


    /**
     * Setting the boolean paused to true to stop ball movement
     *
     * @param paused the paused
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Pausing the ball movement
     */
    public void pause() {
        setPaused(true);
    }

    /**
     * Setting the boolean paused to false in order to resume ball movement
     */
    public void resume() {
        setPaused(false);
    }

    /**
     * Sets timer.
     *
     * @param timer the timer
     */
    public void setTimer(int timer) {
        this.timer = timer;
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
     * Sets bounces.
     *
     * @param bounces the bounces
     */
    public void setBounces(int bounces) {
        this.bounces = bounces;
    }

    /**
     * Gets max bounces.
     *
     * @return the max bounces
     */
    public int getMaxBounces() {
        return maxBounces;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public double getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * Gets speed x.
     *
     * @return the speed x
     */
    public double getSpeedX() {
        return speedX;
    }

    /**
     * Sets speed x.
     *
     * @param speedX the speed x
     */
    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    /**
     * Gets speed y.
     *
     * @return the speed y
     */
    public double getSpeedY() {
        return speedY;
    }

    /**
     * Pause ball boolean.
     *
     * @return the boolean
     */
    public boolean pauseBall() {
        return false;
    }

    /**
     * Sets speed y.
     *
     * @param speedY the speed y
     */
    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Moves the ball using the current speed.
     * It updates the ball and checks for collisions with rackets.
     * It handles collisions with the top and bottom sides of the screen.
     * The ball's movement is changed as a result of its speed and direction,
     * It can bounce off rackets and the top/bottom of the screen.
     */
    public void move() {
        if (!paused) {
            // ensuring everything is updated in case settings are changed mid-game
            speedX = getSpeedX();
            speedY = getSpeedY();

            x += speedX;
            y += speedY;

            // racket collision
            if (game.getPlayer(1).getRacket().collidesWithBall(this, game.getGameCanvas().getWidth(), game.getGameCanvas().getHeight()) ||
                    game.getPlayer(2).getRacket().collidesWithBall(this, game.getGameCanvas().getWidth(), game.getGameCanvas().getHeight())) {
                speedX = -speedX;
            }

            /// top and bottom collision
            if (y <= 0 || y + size >= game.getGameCanvas().getHeight()) {
                speedY = -speedY; // reverse direction

                // increasing speed after a set amount of bounces
                if (collidesWithRacket(game.getPlayer(1).getRacket()) || collidesWithRacket(game.getPlayer(2).getRacket()) ||
                        y <= 0 || y + size >= game.getGameCanvas().getHeight()) {
                    bounces++;
                    if (bounces >= maxBounces) {
                        increaseSpeed();
                        bounces = 0; // Reset counter
                    }
                }
            }
        }
    }

    /**
     * Checking if the ball has collided with the racket
     * This returns a boolean depending on whether it is true or false
     *
     * @param racket the racket
     * @return the boolean
     */
    public boolean collidesWithRacket(Racket racket) {

        return x + size >= racket.getPosX() &&
                x <= racket.getPosX() + racket.getWidth() &&
                y + size >= racket.getPosY() &&
                y <= racket.getPosY() + racket.getHeight();
    }

    /**
     * Increase speed of the ball when called
     */
    public void increaseSpeed() {
        speedX *= 1.1; // increase speed by 10%
        speedY *= 1.1;
    }
}
