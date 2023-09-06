/**
 * Wall.java
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gridobjects;

public class Wall {

    private boolean isDamaged;

    public Wall() {
        isDamaged = false;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public void damageWall() {
        isDamaged = true;
    }

}
