package edu.curtin.saed.assignment1.gridObjects;
import java.awt.Point;
import java.util.Random;

/* Robot class stores coordinates */
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

    public boolean canMoveTo(Point newPosition, Point citadel, Point[][] grid) {
        if (newPosition.equals(citadel)) {
            return false;
        }

        if (grid[newPosition.x][newPosition.y] != null) {
            return false;
        }

        return true;
    }

    //TODO: math operations here may not work as well with double
    public void tryMove(Point[][] grid, Point citadel) {
        Point[] possibleMoves = {
            new Point(getPosition().x + 1, getPosition().y),
            new Point(getPosition().x - 1, getPosition().y),
            new Point(getPosition().x, getPosition().y + 1),
            new Point(getPosition().x, getPosition().y - 1)
        };

        for (Point possibleMove : possibleMoves) {
            if (canMoveTo(possibleMove, citadel, grid)) {
                move(possibleMove);
                return;
            }
        }
    }
}
