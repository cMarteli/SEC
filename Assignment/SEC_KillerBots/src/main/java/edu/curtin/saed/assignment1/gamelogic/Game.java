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
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game implements ArenaListener {

    /**
     *
     */
    private static final int WALL_BUILD_DELAY = 2000;
    /* Class constants */
    private static final int SPAWN_RATE_MS = 1500; // In milliseconds
    private static final int BOT_POINTS = 100; // Amount of points won per bot destroyed

    /* Class variables */
    private final Grid grid;
    private final JFXArena arena;
    private final ExecutorService botThreadPool = Executors.newFixedThreadPool(20); // max 20 bot threads
    private BlockingQueue<Wall> wallQueue = new ArrayBlockingQueue<>(5); // max 5 walls can be queued
    private final ScheduledExecutorService wallScheduler = Executors.newScheduledThreadPool(1); // limits to 1 thread
    private final Random random = new Random();
    private final AtomicInteger score = new AtomicInteger(0); // atomic integer for points

    private volatile boolean running = false; // Flag for game status

    public Game(JFXArena a, Grid g) {
        arena = a;
        grid = g;
    }

    /**
     * Initializes the game and starts the bot spawning thread.
     */
    public void initGame() {
        running = true;
        Thread addBotThread = createBotSpawningThread();
        addBotThread.start();
        initWallScheduler();
    }

    /**
     * Stops the game and cleans up threads.
     */
    public void stopGame() {
        running = false;
        botThreadPool.shutdown();
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

    /*
     * OnClick Listener from JFXArena
     * Queus a new wall to be added to the grid
     */
    @Override
    public void squareClicked(int x, int y) {
        if (grid.isCellEmpty(x, y)) {
            Wall w = new Wall(x, y);
            // Add the wall to the queue instead of directly to the grid
            try {
                wallQueue.put(w);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            arena.printToLogger("New wall queued at: " + x + ", " + y);
        }
    }

    /* Actually add the wall to the grid and update the graphical representation */
    private void buildWall(Wall w) {
        grid.updateObjectPosition(w, w.getX(), w.getY());
        arena.updateWallPosition(w);
    }

    /**
     * Creates a scheduler to add walls to the grid at regular intervals
     */
    private void initWallScheduler() {
        wallScheduler.scheduleAtFixedRate(() -> {
            synchronized (grid) {
                try {
                    if (wallQueue.size() > 0 && grid.getWallCount() < 10) {
                        Wall w = wallQueue.poll();
                        if (grid.isCellEmpty(w.getX(), w.getY())) {
                            buildWall(w);
                            grid.incrementWallCount();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, WALL_BUILD_DELAY, TimeUnit.MILLISECONDS);
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
            grid.updateObjectPosition(newBot, chosenCorner); // add new bot to grid
            botThreadPool.submit(new BotHandler(grid, newBot, this)); // Add new bot to thread pool
        }
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
