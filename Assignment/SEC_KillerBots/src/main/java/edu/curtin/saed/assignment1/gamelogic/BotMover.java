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
    private Point citadel;
    private static final ConcurrentHashMap<Point, Boolean> occupiedCells = new ConcurrentHashMap<>();

    public BotMover(Grid inGrid, Bot inBot) {
        grid = inGrid;
        citadel = grid.getCitadelLocation();
        bot = inBot;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                decideNextMove();
                // animateBotMovement();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /* Returns map of occupied cells */
    public static ConcurrentHashMap<Point, Boolean> getOccupiedCells() {
        return occupiedCells;
    }

    /*
     * Helper function to animate the bot movement.
     */
    private void animateBotMovement() throws InterruptedException {
        for (double progress = 0.0; progress <= 1.0; progress += 0.04) {
            bot.setAnimationProgress(progress);
            TimeUnit.MILLISECONDS.sleep(16);
        }
        bot.move(bot.getNextPosition());
        TimeUnit.MILLISECONDS.sleep(bot.getDelayValue() - 400);
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
        /* Sorts possibleMoves based on distance from citadel */
        sortMovesByClosest(possibleMoves, citadel);

        for (Point newCoords : possibleMoves) {
            // Check-then-act using atomic operation putIfAbsent
            if (occupiedCells.putIfAbsent(newCoords, true) == null) {
                try {
                    bot.setNextPosition(newCoords); // Perform the move
                    /* Animate the move */
                    for (int i = 1; i <= 10; i++) {
                        bot.setAnimationProgress(i / 10.0);
                        TimeUnit.MILLISECONDS.sleep(40);
                        /* Complete the move */
                        grid.updateBotPosition(bot, newCoords);

                        bot.move(newCoords);
                        bot.setAnimationProgress(0.0);
                    }
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
        if (grid.isWithinBounds(point) && grid.isCellEmpty(point)) {
            possibleMoves.add(point);
        }
    }

}
