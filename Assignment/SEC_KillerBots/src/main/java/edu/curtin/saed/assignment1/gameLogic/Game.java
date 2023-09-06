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

public class Game {

    private Grid grid;
    private JFXArena arena;
    private Point citadel;
    private final int SPAWN_RATE = 1500;
    private ExecutorService botThreadPool = Executors.newFixedThreadPool(20);
    private Random random = new Random();
    private volatile boolean running = true;

    public Game(JFXArena a, Grid g) {
        this.arena = a;
        this.grid = g;
        initializeArena();
    }

    private void initializeArena() {
        // Initialize the arena as before
        arena.setGridSize(grid.getWidth(), grid.getHeight());
        citadel = grid.getCitadelLocation();
        arena.setCitadelPosition(citadel.getX(), citadel.getY());
    }

    public void initGame() {
        Thread addBotThread = createBotSpawningThread();
        addBotThread.start();
    }

    private Thread createBotSpawningThread() {
        return new Thread(() -> {
            while (running) {
                try {
                    TimeUnit.MILLISECONDS.sleep(SPAWN_RATE);
                    spawnBot();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public void stopGame() {
        running = false;
        botThreadPool.shutdown();
    }

    /**
     * Updates the graphical representation on the JFXArena.
     */
    public void updateScreen() {
        GridObject[][] gridArray = grid.getGrid();// Get the grid array from the Grid class

        // Loop through each cell in the grid
        for (int y = 0; y < gridArray.length; y++) {
            for (int x = 0; x < gridArray[0].length; x++) {
                GridObject obj = gridArray[y][x];
                // Check if the grid cell contains a robot
                // In the updateScreen() method of Game.java
                if (obj instanceof Bot) {
                    arena.setRobotPosition((Bot) obj);
                }
                // TODO:Check for other types of GridObjects and update the arena accordingly
            }
        }
        arena.requestLayout(); // Force a layout update on the arena
    }

    /**
     * Adds a bot at a random corner of the grid.
     * Checks if the corners are free before adding a new bot.
     */
    private void spawnBot() {
        // Define the four corners
        Point[] corners = new Point[] {
                new Point(0, 0), // top left
                new Point(0, grid.getHeight() - 1), // bottom left
                new Point(grid.getWidth() - 1, 0), // top right
                new Point(grid.getWidth() - 1, grid.getHeight() - 1) // bottom right
        };

        // Filter out occupied corners
        List<Point> freeCorners = new ArrayList<>();
        for (Point corner : corners) {
            if (grid.isCellEmpty(corner)) {
                freeCorners.add(corner);
            }
        }

        // If there's at least one free corner, add a bot there
        if (!freeCorners.isEmpty()) {
            Point chosenCorner = freeCorners.get(random.nextInt(freeCorners.size()));
            Bot newBot = new Bot(chosenCorner.x, chosenCorner.y);

            // Add the new bot to the grid
            grid.getGrid()[chosenCorner.y][chosenCorner.x] = newBot;

            // Hand over the bot's movement to a BotMover instance and submit to the thread
            // pool
            BotMover botMover = new BotMover(grid, citadel, newBot, running);
            botThreadPool.submit(botMover);
        }
    }

    public void initiateBotMovement(Bot newBot) {
        BotMover botMover = new BotMover(grid, citadel, newBot, running);
        botThreadPool.submit(botMover);
    }
}