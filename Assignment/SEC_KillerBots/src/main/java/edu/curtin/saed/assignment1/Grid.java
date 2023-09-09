/**
 * Grid.java
 * Stores grid information, performs boundary checks
 * and GridObjects positions.
 * Uses a concurrent HashMap to store GridObjects in a thread-safe manner.
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1;

import java.awt.Point;
import java.util.concurrent.ConcurrentHashMap;
import edu.curtin.saed.assignment1.gridobjects.*;

public class Grid {
    /* Class variables */
    private final int width;
    private final int height;
    private Point citadelLocation;
    private Point[] corners;
    /* ConcurrentHashMap to store GridObjects in a thread-safe manner */
    private ConcurrentHashMap<Point, GridObject> gridObjMap;

    /**
     * Constructor to initialize the grid.
     *
     * @param w The grid width.
     * @param h The grid height.
     */
    public Grid(int w, int h) {
        width = w;
        height = h;
        gridObjMap = new ConcurrentHashMap<>();
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
        /* Clear old position */
        gridObjMap.remove(new Point(b.getX(), b.getY()));
        /* Set new position */
        gridObjMap.put(newCoords, b);
    }

    /**
     * Checks if a cell has a robot in it.
     *
     * @param p The point to check.
     * @return True if the cell has bot, false otherwise.
     */
    public boolean cellHasBot(Point p) {
        GridObject obj = gridObjMap.get(p);
        return obj instanceof Bot;
    }

    /**
     * Checks if a cell has a wall in it.
     *
     * @param p The point to check.
     * @return True if the cell has a wall, false otherwise.
     */
    public boolean cellHasWall(Point p) {
        GridObject obj = gridObjMap.get(p);
        return obj instanceof Wall;
    }

    /**
     * Gets the GridObject at a specific point.
     *
     * @param p The point to check.
     * @return The GridObject at the point, or null if empty.
     */
    public GridObject getGridObj(Point p) {
        return gridObjMap.get(p);
    }

    /**
     * Removes a GridObject from the grid.
     *
     * @param obj The GridObject to remove.
     */
    public void removeObj(GridObject obj) {
        gridObjMap.remove(new Point(obj.getX(), obj.getY()));
    }

    /**
     * Checks if a cell is empty.
     *
     * @param p The point to check.
     * @return True if the cell is empty, false otherwise.
     */
    public boolean isCellEmpty(Point p) {
        return !gridObjMap.containsKey(p);
    }

    /**
     * Gets the width of the grid.
     *
     * @return The grid width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the grid.
     *
     * @return The grid height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the citadel location.
     *
     * @return The citadel location.
     */
    public Point getCitadelLocation() {
        return citadelLocation;
    }

    /**
     * Gets the corners of the grid.
     *
     * @return An array of corner points.
     */
    public Point[] getCorners() {
        return corners;
    }

    /**
     * Clears the grid.
     */
    public void clearGrid() {
        gridObjMap.clear();
    }

    /**
     * Gets the ConcurrentHashMap representing the grid.
     *
     * @return The ConcurrentHashMap of the grid.
     */
    public ConcurrentHashMap<Point, GridObject> getGridObjMap() {
        return gridObjMap;
    }

}
