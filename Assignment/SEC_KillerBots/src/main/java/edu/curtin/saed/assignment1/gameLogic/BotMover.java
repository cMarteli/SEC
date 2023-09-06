/**
 * BotMover.java
 * Class to handle the movement of a bot.
 * Calculates shortest path using Manhattan distance.
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1.gameLogic;

import edu.curtin.saed.assignment1.Grid;
import edu.curtin.saed.assignment1.gridObjects.Bot;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BotMover implements Runnable {
    private Grid grid;
    private Bot bot;
    private Point citadel;
    private volatile boolean running;

    public BotMover(Grid grid, Point citadel, Bot bot, boolean running) {
        this.grid = grid;
        this.citadel = citadel;
        this.bot = bot;
        this.running = running;
    }

    @Override
    public void run() {
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                decideNextMove();
                for (double progress = 0.0; progress <= 1.0; progress += 0.04) {
                    bot.setAnimationProgress(progress);
                    TimeUnit.MILLISECONDS.sleep(16);
                }
                bot.move(bot.getNextPosition());
                TimeUnit.MILLISECONDS.sleep(bot.getDelayValue() - 400);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Function to decide the next move for a given bot.
     */
    private void decideNextMove() throws InterruptedException {
        ArrayList<Point> possibleMoves = new ArrayList<>();

        // Check Up
        Point up = new Point(bot.getX(), bot.getY() - 1);
        if (bot.getY() > 0 && grid.isCellEmpty(up)) {
            possibleMoves.add(up);
        }
        // Check Down
        Point down = new Point(bot.getX(), bot.getY() + 1);
        if (bot.getY() < grid.getHeight() - 1 && grid.isCellEmpty(down)) {
            possibleMoves.add(down);
        }
        // Check Left
        Point left = new Point(bot.getX() - 1, bot.getY());
        if (bot.getX() > 0 && grid.isCellEmpty(left)) {
            possibleMoves.add(left);
        }
        // Check Right
        Point right = new Point(bot.getX() + 1, bot.getY());
        if (bot.getX() < grid.getWidth() - 1 && grid.isCellEmpty(right)) {
            possibleMoves.add(new Point(right));
        }
        // If no possible moves, wait instead
        if (possibleMoves.size() == 0) {
            TimeUnit.MILLISECONDS.sleep(bot.getDelayValue());
            return;
        }
        /*
         * Sorts 'possibleMoves' based on Manhattan distance to the citadel.
         * Compares points 'a' and 'b' using their distances from citadel.
         * Result is a sorted list where the first point is the closest one.
         */
        possibleMoves.sort((a, b) -> {
            int distanceA = Math.abs(a.x - citadel.x) + Math.abs(a.y - citadel.y);
            int distanceB = Math.abs(b.x - citadel.x) + Math.abs(b.y - citadel.y);
            return Integer.compare(distanceA, distanceB);
        });

        Point newCoords = possibleMoves.get(0);
        bot.setNextPosition(newCoords);

        // Animate the move
        for (int i = 1; i <= 10; i++) {
            bot.setAnimationProgress(i / 10.0);
            TimeUnit.MILLISECONDS.sleep(40);
        }

        // Complete the move
        grid.getGrid()[bot.getY()][bot.getX()] = null;
        grid.getGrid()[newCoords.y][newCoords.x] = bot;
        bot.move(newCoords);
        bot.setAnimationProgress(0.0);
    }
}
