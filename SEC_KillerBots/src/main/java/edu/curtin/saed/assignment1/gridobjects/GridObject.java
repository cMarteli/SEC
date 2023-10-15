/**
 * GridObject.java
 * Abstract class to manage shared attributes for Bot and Wall
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gridobjects;

import java.awt.Point;

public abstract class GridObject {
    /* Class variables */
    protected Point position;
    protected boolean destroyed;

    /* Constructor takes in x and y coordinates as an int */
    public GridObject(int x, int y) {
        position = new Point(x, y);
        destroyed = false; // default
    }

    /* Alternate Constructor takes in point */
    public GridObject(Point p) {
        position = new Point(p);
    }

    public abstract int getId();

    /* Returns position as a point */
    public Point getPosition() {
        return position;
    }

    /* sets position given x and y coordinates */
    public void setPosition(double x, double y) {
        position.setLocation(x, y);
    }

    /* sets position given point */
    public void setPosition(Point p) {
        position.setLocation(p);
    }

    public int getY() {
        return (int) this.position.getY();
    }

    public int getX() {
        return (int) this.position.getX();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }

}
