package model;

import view.GameCanvas;

import java.io.Serializable;

/**
 * The type Racket.
 */
public class Racket extends GameObject implements Serializable {
    private double width;
    private double height;
    private double posX;
    private double posY;
    private GameCanvas gameCanvas;
    private Game game;

    private boolean paused = false;

    /**
     * Instantiates a new Racket.
     * This is how the player will interact with the game.
     * They can move up or down to bounce the ball back to the other player.
     *
     * @param x      the x
     * @param y      the y
     * @param width  the width
     * @param height the height
     * @param game   the game
     */
    public Racket(double x, double y, double width, double height, Game game) {
        super(x, y);
        this.posX = x;
        this.posY = y;
        this.width = width;
        this.height = height;
        this.game = game;
    }

    /**
     * Collides with ball boolean.
     * This gets the updates racket positions
     * It checks if the racket collides with the ball.
     *
     * @param ball         the ball
     * @param canvasWidth  the canvas width
     * @param canvasHeight the canvas height
     * @return the boolean
     */
    public boolean collidesWithBall(Ball ball, double canvasWidth, double canvasHeight) {
       // calculating the racket's boundaries using current canvas dimensions
       double racketLeft = getPosX() * canvasWidth;
       double racketRight = (getPosX() + getWidth()) * canvasWidth;
       double racketTop = getPosY() * canvasHeight;
       double racketBottom = (getPosY() + getHeight()) * canvasHeight;

       return ball.getX() < racketRight &&
               ball.getX() + ball.getSize() > racketLeft &&
               ball.getY() < racketBottom &&
               ball.getY() + ball.getSize() > racketTop;
   }
    /**
     * Pause the racket movement.
     */
    public void pause() {
        paused = true;
    }

    /**
     * Resume the racket movement.
     */
    public void resume() {
        paused = false;
    }
    /**
     * Gets width.
     *
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(double height) {
        this.height = height;
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
     * Move the racket down the canvas.
     */
    public void moveDown() {
        if (!paused && getPosY() + getHeight() < game.getGameCanvas().getHeight() - 65) {
            setPosY(getPosY() + 10);
        }
    }

    /**
     * Move the racket up the canvas.
     */
    public void moveUp() {
        if (!paused && getPosY() > 0) {
            setPosY(getPosY() - 10);
        }
    }

    /**
     * Sets pos y.
     *
     * @param posY the pos y
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }

    /**
     * Gets pos y.
     *
     * @return the pos y
     */
    public double getPosY() {
        return posY;
    }

    /**
     * Sets pos x.
     *
     * @param posX the pos x
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * Gets pos x.
     *
     * @return the pos x
     */
    public double getPosX() {
        return posX;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public double getHeight() {
        return height;
    }

}
