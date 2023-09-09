/**
 * Wall.java
 * Class representing a wall
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gridobjects;

public class Wall extends GridObject {

    private boolean isDamaged;
    private int wallID;
    private static int wallCounter = 0;

    public Wall(int x, int y) {
        super(x, y);
        wallID = wallCounter++;
        isDamaged = false;
    }

    @Override
    public int getId() {
        return wallID;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public void damageWall() {
        isDamaged = true;
    }

}
