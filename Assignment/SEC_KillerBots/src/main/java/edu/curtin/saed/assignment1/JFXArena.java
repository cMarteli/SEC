/**
 * JFXArena.java
 * A JavaFX GUI element that displays a grid on which you can draw images, text and lines.
 * 2023/SEC Assignment 1
 * Code adapted from: Software Engineering Concepts JavaFXDemo by David Cooper
 * Modified: September, 2023 by Victor Marteli 
 */

package edu.curtin.saed.assignment1;

import javafx.scene.canvas.*;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.*;

public class JFXArena extends Pane
{
    // Represents an image to draw, retrieved as a project resource.
    private Image botImg;
    private Image citadelImg;

    // Holds grid dimension values
    private int gridWidth;
    private int gridHeight;

    //list of robot positions
    private List<double[]> robotPositions = new ArrayList<>();

    //Center coordinates of the grid
    private double citadelX;
    private double citadelY;

    private double gridSquareSize; // Auto-calculated
    private Canvas canvas; // Used to provide a 'drawing surface'.

    private List<ArenaListener> listeners = null;

    /**
     * Creates a new arena object, loading the robot image and initialising a drawing surface.
     */
    public JFXArena()
    {
        // Here's how (in JavaFX) you get an Image object from an image file that's part of the
        // project's "resources". If you need multiple different images, you can modify this code
        // accordingly.

        // (NOTE: _DO NOT_ use ordinary file-reading operations here, and in particular do not try
        // to specify the file's path/location. That will ruin things if you try to create a
        // distributable version of your code with './gradlew build'. The approach below is how a
        // project is supposed to read its own internal resources, and should work both for
        // './gradlew run' and './gradlew build'.)

        //Load images
        citadelImg = loadImage(Graphics.CITADEL_IMAGE);
        botImg = loadImage(Graphics.ROBOT_IMAGE);

        gridWidth = 0;
        gridHeight = 0;
        citadelX = 0;
        citadelY = 0;

        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);
    }

    public void setGridSize(int width, int height) {
        gridWidth= width;
        gridHeight = height;
    }

    public void setCitadelPosition(Double x, Double y) {
        citadelX = x;
        citadelY = y;
    }

    /**
     * Loads images in a separate method
     * @return Image object
     * @throws AssertionError if image file cannot be found
     */
    private Image loadImage(String resourceName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new AssertionError("Cannot find image file " + resourceName);
            }
            return new Image(is);
        } catch (IOException e) {
            throw new AssertionError("Cannot load image file " + resourceName, e);
        }
    }


    /**
     * Adds a new robot position or updates an existing one.
     * @param x X-coordinate of the robot.
     * @param y Y-coordinate of the robot.
     * @param id Identifier for the robot. If a robot with this ID already exists, its position is updated.
     */
    public void setRobotPosition(double x, double y, int id) {
        // Check if a robot with this ID already exists
        if (id < robotPositions.size()) {
            robotPositions.get(id)[0] = x;
            robotPositions.get(id)[1] = y;
        } else {
            // Add new robot position
            robotPositions.add(new double[]{x, y});
        }
        requestLayout();
    }

    /**
     * Adds a callback for when the user clicks on a grid square within the arena. The callback
     * (of type ArenaListener) receives the grid (x,y) coordinates as parameters to the
     * 'squareClicked()' method.
     */
    public void addListener(ArenaListener newListener)
    {
        if(listeners == null)
        {
            listeners = new LinkedList<>();
            setOnMouseClicked(event ->
            {
                int gridX = (int)(event.getX() / gridSquareSize);
                int gridY = (int)(event.getY() / gridSquareSize);

                if(gridX < gridWidth && gridY < gridHeight)
                {
                    for(ArenaListener listener : listeners)
                    {
                        listener.squareClicked(gridX, gridY);
                    }
                }
            });
        }
        listeners.add(newListener);
    }


    /**
     * This method is called in order to redraw the screen, either because the user is manipulating
     * the window, OR because you've called 'requestLayout()'.
     *
     * You will need to modify the last part of this method; specifically the sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
     */
    @Override
    public void layoutChildren()
    {
        super.layoutChildren();
        GraphicsContext gfx = canvas.getGraphicsContext2D();
        gfx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());

        // Fills the background
        gfx.setFill(Graphics.BG_COLOUR); //changed this colour to black
        gfx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
            getWidth() / (double) gridWidth,
            getHeight() / (double) gridHeight);

        double arenaPixelWidth = gridWidth * gridSquareSize;
        double arenaPixelHeight = gridHeight * gridSquareSize;


        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setStroke(Graphics.GRID_COLOUR); // changed the colour here to neon blue
        gfx.strokeRect(0.0, 0.0, arenaPixelWidth - 1.0, arenaPixelHeight - 1.0); // Outer edge

        for(int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            double x = (double) gridX * gridSquareSize;
            gfx.strokeLine(x, 0.0, x, arenaPixelHeight);
        }

        for(int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            double y = (double) gridY * gridSquareSize;
            gfx.strokeLine(0.0, y, arenaPixelWidth, y);
        }

        // Draw the citadel at the center
        drawImage(gfx, citadelImg, citadelX, citadelY);
        //drawLabel(gfx, "Citadel", citadelX, citadelY);

        // Loop through the list of robot positions and draw each one
        for (int i = 0; i < robotPositions.size(); i++) {
            double[] position = robotPositions.get(i);
            drawImage(gfx, botImg, position[0], position[1]);
            drawLabel(gfx, "Robot " + (i + 1), position[0], position[1]);
        }
    }


    /**
     * Draw an image in a specific grid location. *Only* call this from within layoutChildren().
     *
     * Note that the grid location can be fractional, so that (for instance), you can draw an image
     * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
     *
     * You shouldn't need to modify this method.
     */
    private void drawImage(GraphicsContext gfx, Image image, double gridX, double gridY)
    {
        // Get the pixel coordinates representing the centre of where the image is to be drawn.
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;

        // We also need to know how "big" to make the image. The image file has a natural width
        // and height, but that's not necessarily the size we want to draw it on the screen. We
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = botImg.getWidth();
        double fullSizePixelHeight = botImg.getHeight();

        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as
            // wide as a full grid cell, and the height will be set to preserve the aspect
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else
        {
            // Otherwise, it's the other way around -- full height, and width is set to
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // Actually put the image on the screen.
        gfx.drawImage(image,
            x - displayedPixelWidth / 2.0,  // Top-left pixel coordinates.
            y - displayedPixelHeight / 2.0,
            displayedPixelWidth,              // Size of displayed image.
            displayedPixelHeight);
    }


    /**
     * Displays a string of text underneath a specific grid location. *Only* call this from within
     * layoutChildren().
     *
     * You shouldn't need to modify this method.
     */
    private void drawLabel(GraphicsContext gfx, String label, double gridX, double gridY)
    {
        gfx.setTextAlign(TextAlignment.CENTER);
        gfx.setTextBaseline(VPos.TOP);
        gfx.setStroke(Color.BLUE);
        gfx.strokeText(label, (gridX + 0.5) * gridSquareSize, (gridY + 1.0) * gridSquareSize);
    }

    /**
     * Draws a (slightly clipped) line between two grid coordinates.
     *
     * You shouldn't need to modify this method.
     */
    private void drawLine(GraphicsContext gfx, double gridX1, double gridY1, double gridX2, double gridY2)
    {
        gfx.setStroke(Color.RED);

        // Recalculate the starting coordinate to be one unit closer to the destination, so that it
        // doesn't overlap with any image appearing in the starting grid cell.
        final double radius = 0.5;
        double angle = Math.atan2(gridY2 - gridY1, gridX2 - gridX1);
        double clippedGridX1 = gridX1 + Math.cos(angle) * radius;
        double clippedGridY1 = gridY1 + Math.sin(angle) * radius;

        gfx.strokeLine((clippedGridX1 + 0.5) * gridSquareSize,
                       (clippedGridY1 + 0.5) * gridSquareSize,
                       (gridX2 + 0.5) * gridSquareSize,
                       (gridY2 + 0.5) * gridSquareSize);
    }
}
