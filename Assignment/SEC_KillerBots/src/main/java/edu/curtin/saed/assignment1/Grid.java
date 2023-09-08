/**
 * Grid.java
 * Stores grid information, performs boundary checks
 * and GridObjects positions.
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1;

import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;

import edu.curtin.saed.assignment1.gridobjects.*;

public class Grid {

    private GridObject[][] gridObjArray;
    private int width;
    private int height;
    private Point citadelLocation;
    private Point[] corners;

    private AtomicInteger wallCount = new AtomicInteger(0);

    /**
     * Constructor to initialize the grid.
     *
     * @param w The grid width.
     * @param h The grid height.
     */
    public Grid(int w, int h) {
        width = w;
        height = h;
        gridObjArray = new GridObject[height][width];
        initializeGrid();
    }

    /**
     * Initializes the grid and sets the corners and citadel location.
     */
    private void initializeGrid() {
        setCorners();
        setCitadelLocation();
    }

    /**
     * Sets the corner points of the grid.
     */
    private void setCorners() {
        corners = new Point[] {
                new Point(0, 0), // Top left
                new Point(width - 1, 0), // Top right
                new Point(0, height - 1), // Bottom left
                new Point(width - 1, height - 1) // Bottom right
        };
    }

    /**
     * Calculates and sets the citadel location at the center of the grid.
     */
    private void setCitadelLocation() {
        int centerX = (width - 1) / 2;
        int centerY = (height - 1) / 2;
        citadelLocation = new Point(centerX, centerY);
    }

    /**
     * Checks if a point is within the boundaries of the grid.
     *
     * @param p The point to check.
     * @return True if the point is within bounds, false otherwise.
     */
    public boolean isWithinBounds(Point p) {
        return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
    }

    /**
     * Updates a GridObject's coordinates in the grid.
     *
     * @param b         The GridObject to update.
     * @param newCoords The new coordinates.
     */
    public void updateObjectPosition(GridObject b, Point newCoords) {
        gridObjArray[b.getY()][b.getX()] = null; // Clear old position
        gridObjArray[newCoords.y][newCoords.x] = b; // Set new position
    }

    /**
     * Updates a GridObject's coordinates in the grid.
     *
     * @param b   The GridObject to update.
     * @param x,y The new coordinates in double format.
     */
    public synchronized void updateObjectPosition(GridObject obj, double x, double y) {
        Point newCoords = new Point();
        newCoords.setLocation(x, y);
        gridObjArray[obj.getY()][obj.getX()] = null; // Clear old position
        gridObjArray[newCoords.y][newCoords.x] = obj; // Set new position
    }

    /**
     * Checks if a has a robot in it.
     *
     * @param p The point to check.
     * @return True if the cell has bot, false otherwise.
     */
    public boolean cellHasBot(Point p) {
        boolean cellHasBot = gridObjArray[p.y][p.x] instanceof Bot;
        return cellHasBot;
    }

    public boolean cellHasWall(Point p) {
        boolean cellHasWall = gridObjArray[p.y][p.x] instanceof Wall;
        return cellHasWall;
    }

    /* Removes a GridObject from the grid */
    public synchronized void removeObj(GridObject obj) {
        gridObjArray[obj.getY()][obj.getX()] = null;
    }

    /**
     * Checks if a cell is empty.
     *
     * @param p The point to check.
     * @return True if the cell is empty, false otherwise.
     */
    public synchronized boolean isCellEmpty(Point p) {
        return gridObjArray[p.y][p.x] == null;
    }

    /**
     * Checks if a cell is empty.
     *
     * @param p The point to check.
     * @return True if the cell is empty, false otherwise.
     */
    public boolean isCellEmpty(int x, int y) {
        return gridObjArray[y][x] == null;
    }

    public GridObject[][] getGridObjArray() {
        return gridObjArray;
    }

    /* Returns null if no objects found */
    public GridObject getGridObj(double x, double y) {
        Point p = new Point();
        p.setLocation(x, y);
        if (!isCellEmpty(p)) { // if there's something there

        }
        return null;
    }

    public void incrementWallCount() {
        wallCount.incrementAndGet();
    }

    public int getWallCount() {
        return wallCount.get();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getCitadelLocation() {
        return citadelLocation;
    }

    public Point[] getCorners() {
        return corners;
    }
}
