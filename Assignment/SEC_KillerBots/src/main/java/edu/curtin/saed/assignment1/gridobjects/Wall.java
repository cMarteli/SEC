/**
 * Wall.java
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gridobjects;

public class Wall extends GridObject {

    private boolean isDamaged;

    public Wall(double x, double y) { // TODO: might need to change to int for consistency with Bot
        super(x, y);
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public void damageWall() {
        isDamaged = true;
    }

}
