/**
 * JFXArena.java
 * A JavaFX GUI element that displays a grid on which you can draw images, text and lines.
 * 2023/SEC Assignment 1
 * Code adapted from: Software Engineering Concepts JavaFXDemo by David Cooper
 * Modified: September, 2023 by Victor Marteli
 */

package edu.curtin.saed.assignment1.jfx;

import javafx.scene.canvas.*;
import javafx.scene.control.TextArea;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.*;

import edu.curtin.saed.assignment1.Grid;
import edu.curtin.saed.assignment1.gridobjects.*;

public class JFXArena extends Pane {
    // Represents an image to draw, retrieved as a project resource.
    private Image botImg;
    private Image citadelImg;
    private Image wallImg;
    private Image walldmgImg;

    private Grid grid;
    // Holds grid dimension values
    private int gridWidth;
    private int gridHeight;
    // Center coordinates of the grid
    private double citadelX;
    private double citadelY;

    private List<Bot> robotPositions = new ArrayList<>();// list of robot positions
    private List<Wall> wallPositions = new ArrayList<>();// list of wall positions

    private double gridSquareSize; // Auto-calculated
    private Canvas canvas; // Used to provide a 'drawing surface'.
    private TextArea logger;

    private List<ArenaListener> listeners = new ArrayList<>();

    /**
     * Creates a new arena object, loading the robot image and initialising a
     * drawing surface.
     */
    public JFXArena(Grid g, TextArea log) {
        logger = log; // passes the logger to the arena
        grid = g;
        // Load images
        citadelImg = loadImage(Graphics.CITADEL_IMAGE);
        botImg = loadImage(Graphics.ROBOT_IMAGE);
        wallImg = loadImage(Graphics.WALL_IMAGE);
        walldmgImg = loadImage(Graphics.WALLDMG_IMAGE);

        gridWidth = grid.getWidth();
        gridHeight = grid.getHeight();

        citadelX = grid.getCitadelLocation().getX();
        citadelY = grid.getCitadelLocation().getY();

        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);
    }

    /**
     * Loads images from the resources folder.
     *
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
     * Prints a message to the logger.
     *
     * @param text
     */
    public void printToLogger(String text) {
        logger.appendText(text + "\n");
    }

    /**
     * Adds a new wall on click. TODO: check method
     *
     * @param w the wall to add or update
     */
    public void updateWallPosition(Wall w) {
        int id = w.getId();
        boolean isNew = false;

        if (id < wallPositions.size()) {
            wallPositions.set(id, w);
        } else {
            wallPositions.add(w);
            isNew = true; // Marks as a new wall
        }

        if (isNew) {
            logger.appendText("New wall built at position (" + w.getX() + ", " + w.getY() + ")\n");
        }
        requestLayout();
    }

    /**
     * Adds a new robot position or updates an existing one.
     *
     * @param b The bot to set or update.
     */
    public void updateBotPosition(Bot b) {
        int id = b.getId();
        boolean isNewBot = false;

        if (id < robotPositions.size()) {
            robotPositions.set(id, b);
        } else {
            robotPositions.add(b);
            isNewBot = true; // Marks as a new bot for logger
        }

        if (isNewBot) {
            logger.appendText("New bot spawned at position (" + b.getX() + ", " + b.getY() + ")\n");
        }
        requestLayout();
    }

    public void clearRobotPosition(Bot b) {
        int id = b.getId() + 1;
        logger.appendText("Bot_" + id + " has been destroyed!\n");
        robotPositions.remove(b);
    }

    // /**
    // * Adds a new wall on click.
    // *
    // * @param x y mouse click coordinates
    // */
    // public void buildWall(double x, double y) {
    // int intx = (int) x;
    // int inty = (int) y;

    // GridObject obj = grid.getGridObj(x, y);
    // if (grid.isCellEmpty(intx, inty)) {
    // obj = new Wall(x, y);
    // wallPositions.add(new Wall(x, y));
    // grid.updateObjectPosition(obj, x, y); // TODO: this makes bots avoid the
    // walls
    // logger.appendText("Wall built at position (" + intx + ", " + inty + ")\n");
    // } else {
    // logger.appendText("There's something there...\n");
    // }
    // }

    public void addListener(ArenaListener newListener) {
        if (listeners.isEmpty()) {
            setOnMouseClicked(event -> {
                int gridX = (int) (event.getX() / gridSquareSize);
                int gridY = (int) (event.getY() / gridSquareSize);

                if (gridX < gridWidth && gridY < gridHeight) {
                    for (ArenaListener listener : listeners) {
                        listener.squareClicked(gridX, gridY);
                        // buildWall((double) gridX, (double) gridY); TODO: remove this
                    }
                }
            });
        }
        listeners.add(newListener);
    }

    /**
     * This method is called in order to redraw the screen, either because the user
     * is manipulating
     * the window, OR because you've called 'requestLayout()'.
     *
     * You will need to modify the last part of this method; specifically the
     * sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else
     * about it.
     */
    @Override
    public void layoutChildren() {
        super.layoutChildren();
        GraphicsContext gfx = canvas.getGraphicsContext2D();
        gfx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());

        // Fills the background
        gfx.setFill(Graphics.BG_COLOUR); // changed this colour to black
        gfx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // First, calculate how big each grid cell should be, in pixels. (We do need to
        // do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
                getWidth() / (double) gridWidth,
                getHeight() / (double) gridHeight);

        double arenaPixelWidth = gridWidth * gridSquareSize;
        double arenaPixelHeight = gridHeight * gridSquareSize;

        // Draw the arena grid lines. This may help for debugging purposes, and just
        // generally
        // to see what's going on.
        gfx.setStroke(Graphics.GRID_COLOUR); // changed the colour here to neon blue
        gfx.strokeRect(0.0, 0.0, arenaPixelWidth - 1.0, arenaPixelHeight - 1.0); // Outer edge

        for (int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            double x = (double) gridX * gridSquareSize;
            gfx.strokeLine(x, 0.0, x, arenaPixelHeight);
        }

        for (int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            double y = (double) gridY * gridSquareSize;
            gfx.strokeLine(0.0, y, arenaPixelWidth, y);
        }

        /* Draws the citadel at the center */
        drawImage(gfx, citadelImg, citadelX, citadelY);
        // drawLabel(gfx, "Citadel", citadelX, citadelY);

        /* Loop through the list of robot positions and draw each one */
        for (int i = 0; i < robotPositions.size(); i++) {
            Bot bot = robotPositions.get(i);
            double x = bot.getX();
            double y = bot.getY();
            double nextX = bot.getNextPosition().getX();
            double nextY = bot.getNextPosition().getY();
            double progress = bot.getAnimationProgress();

            // Calculate the interpolated position for the bot
            double renderX = x + (nextX - x) * progress;
            double renderY = y + (nextY - y) * progress;

            drawImage(gfx, botImg, renderX, renderY);
            drawLabel(gfx, "Bot_" + (i + 1), renderX, renderY);
        }
        /* Draw the walls based on damage status */
        for (int i = 0; i < wallPositions.size(); i++) {
            Wall w = wallPositions.get(i);
            if (w.isDamaged()) {
                drawImage(gfx, walldmgImg, w.getX(), w.getY());
            } else {
                drawImage(gfx, wallImg, w.getX(), w.getY());
            }
        }

    }

    /**
     * Draw an image in a specific grid location. *Only* call this from within
     * layoutChildren().
     *
     * Note that the grid location can be fractional, so that (for instance), you
     * can draw an image
     * at location (3.5,4), and it will appear on the boundary between grid cells
     * (3,4) and (4,4).
     *
     * You shouldn't need to modify this method.
     */
    private void drawImage(GraphicsContext gfx, Image image, double gridX, double gridY) {
        // Get the pixel coordinates representing the centre of where the image is to be
        // drawn.
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;

        // We also need to know how "big" to make the image. The image file has a
        // natural width
        // and height, but that's not necessarily the size we want to draw it on the
        // screen. Wei
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = botImg.getWidth();
        double fullSizePixelHeight = botImg.getHeight();

        double displayedPixelWidth, displayedPixelHeight;
        if (fullSizePixelWidth > fullSizePixelHeight) {
            // Here, the image is wider than it is high, so we'll display it such that it's
            // as
            // wide as a full grid cell, and the height will be set to preserve the aspect
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        } else {
            // Otherwise, it's the other way around -- full height, and width is set to
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // Actually put the image on the screen.
        gfx.drawImage(image,
                x - displayedPixelWidth / 2.0, // Top-left pixel coordinates.
                y - displayedPixelHeight / 2.0,
                displayedPixelWidth, // Size of displayed image.
                displayedPixelHeight);
    }

    /**
     * Displays a string of text underneath a specific grid location. *Only* call
     * this from within
     * layoutChildren().
     *
     * You shouldn't need to modify this method.
     */
    private void drawLabel(GraphicsContext gfx, String label, double gridX, double gridY) {
        gfx.setTextAlign(TextAlignment.CENTER);
        gfx.setTextBaseline(VPos.TOP);
        gfx.setStroke(Graphics.LABEL_COLOUR);
        gfx.strokeText(label, (gridX + 0.5) * gridSquareSize, (gridY + 1.0) * gridSquareSize);
    }

}
