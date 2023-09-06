package edu.curtin.saed.assignment1.gridObjects;

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
