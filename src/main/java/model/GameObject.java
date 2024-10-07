package model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

import java.io.Serializable;

/**
 * The type Game object.
 */
public class GameObject implements Serializable {
    private double x;
    private double y;

    /**
     * Instantiates a new Game object.
     *
     * @param x the x
     * @param y the y
     */
    public GameObject(double x, double y){
        this.x=x;
        this.y=y;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Gets bounds.
     *
     * @return the bounds
     */
    public Bounds getBounds() {
        return new BoundingBox(getX(), getY(), 0, 0);
    }
}
