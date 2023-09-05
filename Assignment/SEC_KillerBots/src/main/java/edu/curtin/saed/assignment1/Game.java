/**
 * Game.java
 * Game class to manage game logic
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1;

import edu.curtin.saed.assignment1.JFX.JFXArena;
import edu.curtin.saed.assignment1.gridObjects.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.awt.Point;

public class Game {

    private Grid grid;
    private Point citadel;
    JFXArena arena;

    private final int SPAWN_RATE = 1500;
    private volatile boolean running = true; // New flag to control threads
    private final List<Thread> activeThreads = new CopyOnWriteArrayList<>(); // New list to hold active threads
    private BlockingQueue<Bot> bots; // List of bots in the game
    // Create a thread pool with a fixed number of threads
    private ExecutorService botThreadPool = Executors.newFixedThreadPool(20); // 10 threads in the pool
    private Random random = new Random(); // For random bot spawning

    public Game(JFXArena a, int x, int y) {
        arena = a;
        grid = new Grid(x, y);

        a.setGridSize(x, y);
        // Set the citadel position in the arena based on game logic
        citadel = grid.getCitadelLocation();
        arena.setCitadelPosition(citadel.getX(), citadel.getY());

        bots = new LinkedBlockingQueue<>();
    }

    public void initGame() {
        System.out.println("Game started"); // DEBUG

        /* New thread to add bots at corners every 1500 ms */
        Thread addBotThread = new Thread(() -> {
            while (running) {
                try {
                    TimeUnit.MILLISECONDS.sleep(SPAWN_RATE); // Wait for 1500 ms
                    spawnBot(); // Add a bot at a random corner
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Reset the interrupt flag
                }
            }
        });

        activeThreads.add(addBotThread);
        addBotThread.start();
    }

    public void stopGame() {
        // Stop all active threads
        running = false; // Stop the threads
        for (Thread t : activeThreads) {
            t.interrupt(); // Interrupt the thread
        }
        activeThreads.clear(); // Remove all threads from the list
        // Shutdown the thread pool
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

        // Force a layout update on the arena
        arena.requestLayout();
    }

    /**
     * Adds a bot at a random corner of the grid.
     * Checks if the corners are free before adding a new bot.
     */
    private void spawnBot() {
        // Define the four corners
        Point[] corners = new Point[] {
                new Point(0, 0), // top left
                new Point(0, grid.getHeight() - 1), // top right
                new Point(grid.getWidth() - 1, 0), // bottom left
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

            // Add the new bot to the BlockingQueue
            bots.offer(newBot);

            grid.getGrid()[chosenCorner.y][chosenCorner.x] = newBot;

            // Submit the bot's task to the thread pool
            botThreadPool.submit(() -> {
                try {
                    initiateRobotMovement(newBot);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Reset the interrupt flag
                }
            });
        }
    }

    /**
     * Decides the next move for a given bot.
     * It checks all possible moves (up, down, left, right) and chooses the one that
     * is closest to the citadel.
     * If no moves are possible, the bot waits for a specified delay.
     *
     * @param robot The bot for which to decide the next move.
     * @throws InterruptedException if the thread is interrupted.
     */
    public void decideNextMove(Bot robot) throws InterruptedException {
        ArrayList<Point> possibleMoves = new ArrayList<>();

        // Check Up
        Point up = new Point(robot.getX(), robot.getY() - 1);
        if (robot.getY() > 0 && this.grid.isCellEmpty(up)) { // if cell is empty and within boundaries
            possibleMoves.add(up); // add to possible moves list
        }

        // Check Down
        Point down = new Point(robot.getX(), robot.getY() + 1);
        if (robot.getY() < this.grid.getHeight() - 1 && this.grid.isCellEmpty(down)) {
            possibleMoves.add(down);
        }

        Point left = new Point(robot.getX() - 1, robot.getY());
        // Check Left
        if (robot.getX() > 0 && this.grid.isCellEmpty(left)) {
            possibleMoves.add(left);
        }

        // Check Right
        Point right = new Point(robot.getX() + 1, robot.getY());
        if (robot.getX() < this.grid.getWidth() - 1 && this.grid.isCellEmpty(right)) {
            possibleMoves.add(new Point(right));
        }

        // If no possible moves, robot is boxed in. Wait for delayValue ms
        if (possibleMoves.size() == 0) {
            TimeUnit.MILLISECONDS.sleep(robot.getDelayValue());
            return;
        }

        // Prefer to move towards the citadel if possible
        possibleMoves.sort((a, b) -> {
            int distanceA = Math.abs(a.x - this.citadel.x) + Math.abs(a.y - this.citadel.y);
            int distanceB = Math.abs(b.x - this.citadel.x) + Math.abs(b.y - this.citadel.y);
            return Integer.compare(distanceA, distanceB);
        });

        Point newCoords = possibleMoves.get(0);
        robot.setNextPosition(newCoords); // Set the next position

        // Animate the move in 10 steps
        for (int i = 1; i <= 10; i++) {
            robot.setAnimationProgress(i / 10.0);
            TimeUnit.MILLISECONDS.sleep(40); // Sleep for 40 ms between each step
        }

        // Complete the move
        grid.getGrid()[robot.getY()][robot.getX()] = null; // Empty current cell
        grid.getGrid()[newCoords.y][newCoords.x] = robot; // Move to new cell
        robot.move(newCoords); // move robot to new point
        robot.setAnimationProgress(0.0); // Reset animation progress

    }

    // Function to initiate robot movements
    public void initiateRobotMovement(Bot b) throws InterruptedException {
        while (running && !Thread.currentThread().isInterrupted()) {
            this.decideNextMove(b);
            for (double progress = 0.0; progress <= 1.0; progress += 0.04) { // 25 frames
                b.setAnimationProgress(progress);
                TimeUnit.MILLISECONDS.sleep(16); // 16 ms per frame
            }
            b.move(b.getNextPosition());
            TimeUnit.MILLISECONDS.sleep(b.getDelayValue() - 400); // 400 ms already spent in animation
        }
    }

}
