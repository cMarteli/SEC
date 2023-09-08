package edu.curtin.saed.assignment1.gamelogic;

import edu.curtin.saed.assignment1.Grid;
import edu.curtin.saed.assignment1.gridobjects.Bot;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BotSpawner {
    /* Class constants */
    private static final int SPAWN_RATE_MS = 1500; // In milliseconds

    /* Class variables */
    private final Grid grid;
    private final ExecutorService botThreadPool = Executors.newFixedThreadPool(20); // max 20 bot threads
    private final Random random;
    private final Game game;

    public BotSpawner(Grid inGrid, Game inGame) {
        grid = inGrid;
        game = inGame;
        random = new Random();
    }

    /**
     * Creates a thread responsible for spawning bots at regular intervals.
     *
     * @return The bot-spawning Thread.
     */
    public Thread createBotSpawningThread() {
        return new Thread(() -> {
            while (game.isRunning()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(SPAWN_RATE_MS);
                    spawnBot();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            botThreadPool.shutdown();
        });
    }

    /**
     * If free corners are available, randomly spawns a bot at one of them.
     */
    private void spawnBot() {
        /* Bot spawning logic */
        List<Point> freeCorners = getFreeCorners();
        if (!freeCorners.isEmpty()) {
            Point chosenCorner = freeCorners.get(random.nextInt(freeCorners.size()));
            Bot newBot = new Bot(chosenCorner.x, chosenCorner.y);
            grid.updateObjectPosition(newBot, chosenCorner); // add new bot to grid
            botThreadPool.submit(new BotMover(grid, newBot, game)); // Add new bot to thread pool
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
