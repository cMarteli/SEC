/**
 * Wall.java
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gridobjects;

public class Wall extends GridObject {

    private boolean isDamaged;
    private int wallID;
    private static int wallCounter = 0;
    private static final String TYPE = "wall";

    public Wall(double x, double y) { // TODO: might need to change to int for consistency with Bot
        super(x, y);
        wallID = wallCounter++;
        isDamaged = false;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public void damageWall() {
        isDamaged = true;
    }

    public int getId() {
        return wallID;
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
