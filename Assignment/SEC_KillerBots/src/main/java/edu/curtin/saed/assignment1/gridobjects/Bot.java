/**
 * Bot.java
 * Class representing a bot
 * 2023/SEC Assignment 1
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

    public Bot(int x, int y) {
        super(x, y); // Storing location in parent class GridObject
        id = robotCounter++;
        delayValue = random.nextInt(1501) + 500; // generates a delay value between 500 and 2000 ms
    }

    @Override
    public int getId() {
        return id + 1;
    }

    public String name() {
        return "Bot_" + getId();
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

    public double getNextPositionX() {
        return nextPosition.getX();
    }

    public double getNextPositionY() {
        return nextPosition.getY();
    }

    /* Animation helper methods */

    /**
     * Calculates the in between frame x position for the bot.
     *
     * @return The in between frame x position for the bot.
     */
    public double getInterPointX() {
        double x = super.position.getX();
        double nextX = getNextPositionX();
        return (x + (nextX - x) * animationProgress);
    }

    /**
     * Calculates the in between frame y position for the bot.
     *
     * @return The in between frame y position for the bot.
     */
    public double getInterPointY() {
        double y = super.position.getY();
        double nextY = getNextPositionY();
        return (y + (nextY - y) * animationProgress);
    }

    public void setAnimationProgress(double aProg) {
        animationProgress = aProg;
    }

    public double getAnimationProgress() {
        return animationProgress;
    }
}
