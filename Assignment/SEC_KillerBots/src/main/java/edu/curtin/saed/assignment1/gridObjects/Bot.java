package edu.curtin.saed.assignment1.gridObjects;
import java.awt.Point;
import java.util.Random;

/* GridObject class stores coordinates */
public class Bot extends GridObject{

    private int id;
    private int delayValue;
    private static int robotCounter = 1;
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
