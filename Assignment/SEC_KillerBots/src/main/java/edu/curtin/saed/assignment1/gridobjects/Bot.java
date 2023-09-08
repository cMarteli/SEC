/**
 * Bot.java
 * 2023/SEC Assignment 1
 * GridObject parent class stores coordinates
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gridobjects;

import java.awt.Point;
import java.util.Random;

public class Bot extends GridObject {

    private int id;
    private int delayValue;
    private static int robotCounter = 0; // Starting at 1 will cause setRobotPosition() in JFXArena to break
    private Point nextPosition;
    private double animationProgress; // 0.0 to 1.0
    private static Random random = new Random();
    private static final String TYPE = "bot";

    public Bot(int x, int y) {
        super(x, y); // Storing location in parent class GridObject
        id = robotCounter++;
        delayValue = random.nextInt(1501) + 500; // generates a delay value between 500 and 2000 ms
    }

    public int getId() {
        return id;
    }

    public int getDelayValue() {
        return delayValue;
    }

    public void setNextPosition(Point nextPos) {
        nextPosition = nextPos;
    }

    public Point getNextPosition() {
        return nextPosition;
    }

    /* Animation helper methods */
    public void setAnimationProgress(double aProg) {
        animationProgress = aProg;
    }

    public double getAnimationProgress() {
        return animationProgress;
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
