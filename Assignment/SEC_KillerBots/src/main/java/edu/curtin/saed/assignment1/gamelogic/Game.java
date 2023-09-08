/**
 * Game.java
 * Game class to manage game logic
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gamelogic;

import edu.curtin.saed.assignment1.Grid;
import edu.curtin.saed.assignment1.gridobjects.*;
import edu.curtin.saed.assignment1.jfx.ArenaListener;
import edu.curtin.saed.assignment1.jfx.JFXArena;
import java.awt.Point;

import java.util.concurrent.atomic.AtomicInteger;

public class Game implements ArenaListener {

    /* Class constants */
    private static final int BOT_POINTS = 100; // Amount of points won per bot destroyed

    /* Class variables */
    private final Grid grid;
    private final JFXArena arena;

    private volatile boolean running = false; // Game state

    private WallScheduler wallScheduler; // Scheduler for adding walls

    private final AtomicInteger score = new AtomicInteger(0); // atomic integer for score

    public Game(JFXArena inArena, Grid inGrid) {
        arena = inArena;
        grid = inGrid;
    }

    /**
     * Initializes the game and starts the bot spawning thread.
     */
    public void initGame() {
        System.out.println("InitGame: Initializing game...");
        running = true;

        /* Initialize and start BotSpawner */
        BotSpawner botSpawner = new BotSpawner(grid, this);
        Thread addBotThread = botSpawner.createBotSpawningThread();
        addBotThread.start();

        wallScheduler = new WallScheduler(grid, this);
    }

    /**
     * Stops the game and cleans up threads.
     */
    public void stopGame() {
        System.out.println("StopGame: Stopping game...");
        running = false;
        arena.printToLogger("\n*GAME OVER*\nFinal Score:" + getScore() + "\n");
        wallScheduler.shutdown();
    }

    /**
     * Checks if the game is running.
     *
     * @return true if the game is running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Queues a new wall to be added to the grid when a square is clicked.
     */
    @Override
    public void squareClicked(int x, int y) {
        if (grid.isCellEmpty(x, y)) {
            Wall w = new Wall(x, y);
            wallScheduler.addToQueue(w);
            arena.printToLogger("New wall queued at: " + x + ", " + y);
        } else {
            arena.printToLogger("Can't build here.");
        }
    }

    /**
     * Adds a wall to the grid and updates its graphical representation.
     */
    public void buildWall(Wall w) {
        grid.updateObjectPosition(w, w.getX(), w.getY());
        arena.updateWallPosition(w);
    }

    /**
     * Handles a bot collision with a wall.
     *
     * @param newCoords The new coordinates of the bot.
     */
    public void wallCollision(Point newCoords) {
        Wall w = (Wall) grid.getGridObj(newCoords);
        if (w.isDamaged()) {
            grid.removeObj(w);
            // arena.clearWallPosition(w);
        } else {
            w.damageWall();
        }
    }

    /**
     * Updates the score.
     *
     * @param delta The amount to change the score by.
     */
    public void updateScore(int delta) {
        score.addAndGet(delta);
    }

    /**
     * Gets the current score.
     *
     * @return The current score.
     */
    public int getScore() {
        return score.get();
    }

    /**
     * Gets the number of walls in the queue.
     *
     * @return number of walls in queue.
     */
    public int getQueueCount() {
        return wallScheduler.getQueueCount();
    }

    /**
     * Updates the graphical representation on the JFXArena.
     */
    public void updateJFX() {
        GridObject[][] gridArray = grid.getGridObjArray();
        for (GridObject[] row : gridArray) {
            for (GridObject gridObject : row) {
                if (gridObject instanceof Bot) {
                    arena.updateBotPosition((Bot) gridObject);
                }
                if (gridObject instanceof Wall) {
                    arena.updateWallPosition((Wall) gridObject);
                }
            }
        }
        arena.requestLayout();
    }

    /**
     * Removes a bot from the game after it collides with a wall.
     *
     * @param bot The bot to be removed.
     */
    public void removeBot(Bot bot) {
        arena.clearRobotPosition(bot); // Remove bot from JFXArena
        grid.removeObj(bot); // Remove bot from grid
        updateScore(BOT_POINTS); // Update score
    }
}
