package edu.curtin.saed.assignment1.GridObjects;

/* Parent class to manage shared attributes for Bot, Citadel, and Wall */
import java.awt.Point;

public class GridObject {
    protected Point position;
    /* Constructor takes in x and y coordinates*/
    public GridObject(int x, int y) {
        this.position = new Point(x, y);
    }

    /*Alternate Constructor takes in point */
    public GridObject(Point p) {
        this.position = new Point(p);
    }

    /* Returns position as a point */
    public Point getPosition() {
        return position;
    }

    /* sets position given x and y coordinates */
    public void setPosition(int x, int y) {
        this.position.setLocation(x, y);
    }

    /* sets position given point */
    public void setPosition(Point p) {
    this.position.setLocation(p);
    }
}
