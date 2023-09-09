/**
 * BotMover.java
 * Runnable thread that handle the movements of a single bot on the grid.
 * Calculates shortest path from center.
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gamelogic;

import edu.curtin.saed.assignment1.Grid;
import edu.curtin.saed.assignment1.gridobjects.Bot;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BotMover implements Runnable {

    /* Class variables */
    private final Grid grid;
    private final Bot bot;
    private final Game game;
    /*
     * This is used to store the cells the bot is currently taking up whilst moving
     */
    private static final ConcurrentHashMap<Point, Boolean> NON_EMPTY_CELLS = new ConcurrentHashMap<>();

    public BotMover(Grid inGrid, Bot inBot, Game inGame) {
        grid = inGrid;
        bot = inBot;
        game = inGame;
    }

    @Override
    public void run() {
        try {
            while (!bot.isDestroyed() && game.isRunning()) {
                decideNextMove(); // move bot
            }
        } catch (InterruptedException e) {
            System.out.println("BotMover: Interrupted");
        } finally {
            Thread.currentThread().interrupt(); // Interrupt thread if it's not already
        }
    }

    /**
     * Returns true if bot is at citadel.
     *
     * @return
     */
    private boolean isAtCitadel(Point newCoords) {
        return newCoords.equals(grid.getCitadelLocation());
    }

    /* Returns map of occupied cells */
    public static ConcurrentHashMap<Point, Boolean> getOccupiedCells() {
        return NON_EMPTY_CELLS;
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
        if (isAtCitadel(newCoords)) { // if bot is at citadel
            game.stopGame(bot); // Game over
        }
        /* Check for collision with Wall */
        else if (grid.cellHasWall(newCoords)) { // Updated to use new method
            game.wallCollision(newCoords); // do wall collision
            killBot();
            return; // Exit the function
        } else {
            /* Update the bot's position on the grid */
            grid.updateObjectPosition(bot, newCoords);

        }

        /* Complete the move */
        bot.setPosition(newCoords); // Set the new position
        bot.setAnimationProgress(0.0); // Reset animation progress
        TimeUnit.MILLISECONDS.sleep(bot.getDelayValue()); // Add delay after moving
    }

    /**
     * Helper function to calculate the manhattan distance between two points.
     * Uses the formula: |x1 - x2| + |y1 - y2|
     *
     * @param a Point a, the first point
     * @param b Point b, the second point
     */
    private int calculateDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    /**
     * Sorts a list of points by their distance to a center point.
     *
     * @param possibleMoves List of possible moves
     * @param center        point we're finding shortest distance to
     */
    private void sortMovesByClosest(List<Point> possibleMoves, Point center) {
        possibleMoves.sort((a, b) -> {
            int distanceA = calculateDistance(a, center);
            int distanceB = calculateDistance(b, center);
            return Integer.compare(distanceA, distanceB);
        });
    }

    /* Kills bot thread and removes it from grid */
    private void killBot() {
        bot.destroy();
        game.removeBot(bot);
    }

    /**
     * Function to decide the next move for a given bot.
     */
    private void decideNextMove() throws InterruptedException {
        /* Get a list of all possible moves */
        ArrayList<Point> possibleMoves = (ArrayList<Point>) getPossibleMoves();
        /* If no possible moves, wait instead */
        if (possibleMoves.isEmpty()) {
            TimeUnit.MILLISECONDS.sleep(bot.getDelayValue());
            return;
        }
        sortMovesByClosest(possibleMoves, grid.getCitadelLocation()); // Sorts in-place

        for (Point newCoords : possibleMoves) {
            /* Complete the move */
            if (NON_EMPTY_CELLS.putIfAbsent(newCoords, true) == null) { // putIfAbsent() is atomic
                try {
                    bot.setNextPosition(newCoords); // Set the next position
                    animateBotMovement(newCoords); // Animate the move
                } finally {
                    NON_EMPTY_CELLS.remove(newCoords); // Removes the in-use flag
                }
                break; // Successfully moved, break out of loop
            }
        }
    }

    /**
     * Get all possible moves for the bot
     */
    private List<Point> getPossibleMoves() {
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
     * Add point to possible moves if within bounds and has no bot
     */
    private void addIfValidMove(Point point, List<Point> possibleMoves) {
        if (grid.isWithinBounds(point) && !grid.cellHasBot(point)) {
            possibleMoves.add(point);
        }
    }

}
