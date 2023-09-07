/**
 * Game.java
 * Game class to manage game logic
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gamelogic;

import edu.curtin.saed.assignment1.Grid;
import edu.curtin.saed.assignment1.gridobjects.*;
import edu.curtin.saed.assignment1.jfx.JFXArena;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

    /* Class constants */
    private static final int SPAWN_RATE_MS = 1500; // In milliseconds

    /* Class variables */
    private final Grid grid;
    private final JFXArena arena;
    private final Point citadel;
    private final ExecutorService botThreadPool = Executors.newFixedThreadPool(20);
    private final Random random = new Random();
    private final AtomicInteger score = new AtomicInteger(0); // atomic integer for thread safety

    private volatile boolean running = false; // Flag for game status

    public Game(JFXArena arena, Grid grid) {
        this.arena = arena;
        this.grid = grid;
        this.citadel = grid.getCitadelLocation();
        initializeArena();
    }

    /**
     * Initializes the game and starts the bot spawning thread.
     */
    public void initGame() {
        running = true;
        Thread addBotThread = createBotSpawningThread();
        addBotThread.start();
    }

    /**
     * Stops the game and shuts down the thread pool.
     */
    public void stopGame() {
        running = false;
        botThreadPool.shutdown();
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
     * Updates the score by a delta.
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
     * Updates the graphical representation on the JFXArena.
     */
    public void updateJFX() {
        GridObject[][] gridArray = grid.getGridObjArray();
        for (GridObject[] row : gridArray) {
            for (GridObject gridObject : row) {
                if (gridObject instanceof Bot) {
                    arena.setRobotPosition((Bot) gridObject);
                }
                // else if (gridObject instanceof Wall) {
                // arena.setWallPosition((Wall) gridObject); } TODO: Update wall here
            }
        }
        arena.requestLayout();
    }

    /**
     * Initializes the arena by setting its size and citadel position.
     */
    private void initializeArena() {
        arena.setGridSize(grid.getWidth(), grid.getHeight());
        arena.setCitadelPosition(citadel.getX(), citadel.getY());
    }

    /**
     * Creates a thread responsible for spawning bots at regular intervals.
     *
     * @return The bot-spawning Thread.
     */
    private Thread createBotSpawningThread() {
        return new Thread(() -> {
            while (running) {
                try {
                    TimeUnit.MILLISECONDS.sleep(SPAWN_RATE_MS);
                    spawnBot();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    /**
     * If free corners are available, randomly spawns a bot at one of them.
     */
    private void spawnBot() {
        List<Point> freeCorners = getFreeCorners();
        if (!freeCorners.isEmpty()) {
            Point chosenCorner = freeCorners.get(random.nextInt(freeCorners.size()));
            Bot newBot = new Bot(chosenCorner.x, chosenCorner.y);
            grid.getGridObjArray()[chosenCorner.y][chosenCorner.x] = newBot;
            botThreadPool.submit(new BotMover(grid, newBot)); // Add new bot to thread pool
        }
    }

    /**
     * Returns a list of empty corners on the grid.
     *
     * @return List of free corners.
     */
    private List<Point> getFreeCorners() {
        /* First get all corners */
        Point[] corners = grid.getCorners();
        /* Then check which are empty and return them as a list */
        List<Point> freeCorners = new ArrayList<>();
        for (Point corner : corners) {
            if (grid.isCellEmpty(corner)) {
                freeCorners.add(corner);
            }
        }
        return freeCorners;
    }
}
