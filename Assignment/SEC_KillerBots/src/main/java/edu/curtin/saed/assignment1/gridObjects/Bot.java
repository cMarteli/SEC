/**
 * Bot.java
 * 2023/SEC Assignment 1
 * GridObject parent class stores coordinates
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gridObjects;
import java.awt.Point;
import java.util.Random;

public class Bot extends GridObject{

    private int id;
    private int delayValue;
    private static int robotCounter = 0; //Starting at 1 will cause setRobotPosition() in JFXArena to break
    private static Random random = new Random();


    public Bot(int x, int y) {
        super(x,y); // Call the constructor of the parent class GridObject
        this.id = robotCounter++;
        this.delayValue = random.nextInt(1501) + 500;
    }

    public int getId() {
        return id;
    }

    public int getDelayValue() {
        return delayValue;
    }

    public void move(Point newPosition) {
        setPosition(newPosition); // Assuming GridObject has a setPosition method
    }

}
