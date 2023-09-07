/**
 * Grid.java
 * Stores grid information, performs boundary checks
 * and GridObjects positions.
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1;

import java.awt.Point;
import edu.curtin.saed.assignment1.gridobjects.*;

public class Grid {

    private GridObject[][] gridObjArray;
    private int width;
    private int height;
    private Point citadelLocation;
    private Point[] corners;

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
     * Checks if a cell is empty.
     *
     * @param p The point to check.
     * @return True if the cell is empty, false otherwise.
     */
    public boolean isCellEmpty(Point p) {
        return gridObjArray[p.y][p.x] == null;
    }

    public GridObject[][] getGridObjArray() {
        return gridObjArray;
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
