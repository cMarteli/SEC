/**
 * Game.java
 * Game class to manage game logic
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gameLogic;

import edu.curtin.saed.assignment1.Grid;
import edu.curtin.saed.assignment1.JFX.JFXArena;
import edu.curtin.saed.assignment1.gridObjects.*;
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
    private final AtomicInteger score = new AtomicInteger(0);

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
        GridObject[][] gridArray = grid.getGrid();
        for (int y = 0; y < gridArray.length; y++) {
            for (int x = 0; x < gridArray[0].length; x++) {
                if (gridArray[y][x] instanceof Bot) {
                    arena.setRobotPosition((Bot) gridArray[y][x]);
                }
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
     * Spawns a bot at a random, free corner of the grid.
     */
    private void spawnBot() {
        List<Point> freeCorners = getFreeCorners();
        if (!freeCorners.isEmpty()) {
            Point chosenCorner = freeCorners.get(random.nextInt(freeCorners.size()));
            Bot newBot = new Bot(chosenCorner.x, chosenCorner.y);
            grid.getGrid()[chosenCorner.y][chosenCorner.x] = newBot;
            botThreadPool.submit(new BotMover(grid, citadel, newBot, running));
        }
    }

    /**
     * Returns a list of free corners on the grid.
     *
     * @return List of free corners.
     */
    private List<Point> getFreeCorners() {
        Point[] corners = new Point[] {
                new Point(0, 0),
                new Point(0, grid.getHeight() - 1),
                new Point(grid.getWidth() - 1, 0),
                new Point(grid.getWidth() - 1, grid.getHeight() - 1)
        };
        List<Point> freeCorners = new ArrayList<>();
        for (Point corner : corners) {
            if (grid.isCellEmpty(corner)) {
                freeCorners.add(corner);
            }
        }
        return freeCorners;
    }
}
