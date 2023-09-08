/**
 * BotMover.java
 * Class to handle the movements of bots on the grid.
 * Calculates shortest path from center.
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gamelogic;

import edu.curtin.saed.assignment1.Grid;
import edu.curtin.saed.assignment1.gridobjects.Bot;

import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BotMover implements Runnable {
    private Grid grid;
    private Bot bot;
    private Game game;
    private volatile boolean isTerminated = false; // check

    /* ConcurrentHashMap is thread-safe */
    private static final ConcurrentHashMap<Point, Boolean> occupiedCells = new ConcurrentHashMap<>();

    public BotMover(Grid inGrid, Bot inBot, Game inGame) {
        grid = inGrid;
        bot = inBot;
        game = inGame;
    }

    // @Override
    // public void run() {
    // try {
    // while (!Thread.currentThread().isInterrupted()) {
    // decideNextMove();
    // }
    // } catch (InterruptedException e) {
    // Thread.currentThread().interrupt();
    // }
    // }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && !isTerminated) { // Modify this line
                decideNextMove();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Safely terminate this bot, ending its thread.
     */
    public void terminate() {
        isTerminated = true;
    }

    /* Returns map of occupied cells */
    public static ConcurrentHashMap<Point, Boolean> getOccupiedCells() {
        return occupiedCells;
    }

    /**
     * Helper function to animate the bot movement.
     */
    private void animateBotMovement(Point newCoords) throws InterruptedException {
        // Set animation to in-progress
        for (double progress = 0.0; progress <= 1.0; progress += 0.1) {
            bot.setAnimationProgress(progress);
            TimeUnit.MILLISECONDS.sleep(40);
        }
        /* Update the bot's position on the grid */
        grid.updateObjectPosition(bot, newCoords);
        /* Complete the move */
        bot.move(newCoords);
        bot.setAnimationProgress(0.0); // Reset animation progress
        TimeUnit.MILLISECONDS.sleep(bot.getDelayValue()); // Add delay after moving
    }

    /**
     * Helper function to calculate the manhattan distance between two points.
     */
    private int calculateDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    /**
     * Finds the distance from center for each possible move and sorts them.
     *
     * @param possibleMoves ArrayList of possible moves
     * @param center        point we're finding shortest distance to
     */
    private void sortMovesByClosest(ArrayList<Point> possibleMoves, Point center) {
        possibleMoves.sort((a, b) -> {
            int distanceA = calculateDistance(a, center);
            int distanceB = calculateDistance(b, center);
            return Integer.compare(distanceA, distanceB);
        });
    }

    // /**
    // * Function to decide the next move for a given bot.
    // */
    // private void decideNextMove() throws InterruptedException {
    // /* Get a list of all possible moves */
    // ArrayList<Point> possibleMoves = getPossibleMoves();
    // /* If no possible moves, wait instead */
    // if (possibleMoves.isEmpty()) {
    // TimeUnit.MILLISECONDS.sleep(bot.getDelayValue());
    // return;
    // }
    // sortMovesByClosest(possibleMoves, grid.getCitadelLocation()); // Sorts
    // in-place

    // for (Point newCoords : possibleMoves) {
    // /* putIfAbsent() is atomic */
    // if (occupiedCells.putIfAbsent(newCoords, true) == null) {
    // try {
    // bot.setNextPosition(newCoords); // Set the next position
    // animateBotMovement(newCoords); // Animate the move
    // } finally {
    // occupiedCells.remove(newCoords); // Removes the in-use flag
    // }
    // break; // Successfully moved, break out of loop
    // }
    // }
    // }
    /**
     * Function to decide the next move for a given bot.
     */
    private void decideNextMove() throws InterruptedException {
        /* Get a list of all possible moves */
        ArrayList<Point> possibleMoves = getPossibleMoves();
        /* If no possible moves, wait instead */
        if (possibleMoves.isEmpty()) {
            TimeUnit.MILLISECONDS.sleep(bot.getDelayValue());
            return;
        }
        sortMovesByClosest(possibleMoves, grid.getCitadelLocation()); // Sorts in-place

        for (Point newCoords : possibleMoves) {
            /* Check for collision with Wall */
            if (grid.cellHasWall(newCoords)) {
                bot.move(newCoords); // Let the bot complete its move into the square with the wall.
                grid.updateObjectPosition(bot, newCoords); // Update the position in the grid.
                game.removeBot(bot); // Remove the bot from the game
                Thread.currentThread().interrupt(); // Terminate the thread.
                return; // Exit the function, thus ending the loop in the run() method.
            }

            /* putIfAbsent() is atomic */
            if (occupiedCells.putIfAbsent(newCoords, true) == null) {
                try {
                    bot.setNextPosition(newCoords); // Set the next position
                    animateBotMovement(newCoords); // Animate the move
                } finally {
                    occupiedCells.remove(newCoords); // Removes the in-use flag
                }
                break; // Successfully moved, break out of loop
            }
        }
    }

    /**
     * Get all possible moves for the bot
     */
    private ArrayList<Point> getPossibleMoves() {
        ArrayList<Point> possibleMoves = new ArrayList<>();
        // Check Up
        Point up = new Point(bot.getX(), bot.getY() - 1);
        addIfValidMove(up, possibleMoves);
        // Check Down
        Point down = new Point(bot.getX(), bot.getY() + 1);
        addIfValidMove(down, possibleMoves);
        // Check Left
        Point left = new Point(bot.getX() - 1, bot.getY());
        addIfValidMove(left, possibleMoves);
        // Check Right
        Point right = new Point(bot.getX() + 1, bot.getY());
        addIfValidMove(right, possibleMoves);

        return possibleMoves;
    }

    /**
     * Add point to possible moves if it's a valid move
     */
    private void addIfValidMove(Point point, ArrayList<Point> possibleMoves) {
        // if (grid.isWithinBounds(point) && grid.isCellEmpty(point)) {
        // possibleMoves.add(point);
        // }
        if (grid.isWithinBounds(point) && !grid.cellHasBot(point)) {
            possibleMoves.add(point);
        }
    }

}
