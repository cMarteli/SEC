/**
 * Grid.java
 * Stores grid information
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1;

import java.awt.Point;

import edu.curtin.saed.assignment1.gridobjects.*;

public class Grid {

    private GridObject[][] grid;
    private int width;
    private int height;
    private Point citadelLocation;

    public Grid(int width, int height) {
        // Initialize grid as a 2D array
        this.width = width;
        this.height = height;
        this.grid = new GridObject[height][width];

        // Calculates the center coordinates of the grid.
        double centerX = (width - 1) / 2.0;
        double centerY = (height - 1) / 2.0;

        this.citadelLocation = new Point((int) centerX, (int) centerY);
    }

    /**
     * @param robot
     * @param x
     * @param y
     */
    public void placeRobot(Bot robot, int x, int y) {
        if (this.grid[y][x] == null) {
            this.grid[y][x] = robot; // sets robot to that location
            robot.setPosition(x, y); // updates robot internal position
        } else {
            System.out.println("Cannot place robot at (" + x + "," + y + ") as there is already a grid object there.");
        }
    }

    // Function to check if a cell is empty
    public boolean isCellEmpty(Point p) {
        return this.grid[p.y][p.x] == null;
    }

    public GridObject[][] getGrid() {
        return grid;
    }

    // public void clearGrid() {
    // for (int i = 0; i < this.height; i++) {
    // for (int j = 0; j < this.width; j++) {
    // this.grid[i][j] = null;
    // }
    // }
    // }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getCitadelLocation() {
        return citadelLocation;
    }

}
