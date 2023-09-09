/**
 * WallScheduler.java
 * Manages the scheduling of wall building.
 * Uses a BlockingQueue to store walls that are queued to be built.
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */
package edu.curtin.saed.assignment1.gamelogic;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import edu.curtin.saed.assignment1.gridobjects.Wall;

public class WallScheduler {
    /* Class constants */
    private static final int MAX_QUEUE_SIZE = 5; // max 5 walls can be queued
    private static final int MAX_TOTAL_WALLS = 10;
    private static final int BUILD_DELAY = 2000; // How long thread has to wait to build wall in milliseconds
    /* Class variables */
    private Game game;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // limits to 1 thread
    private BlockingQueue<Wall> wallQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
    private AtomicInteger wallCount = new AtomicInteger(0);
    private AtomicInteger wallsQueued = new AtomicInteger(0);

    public WallScheduler(Game inGame) {
        game = inGame;
        initWallScheduler();
    }

    /**
     * Creates a scheduler to add walls to the grid at regular intervals
     */
    private void initWallScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            /* if there are walls in queue and less than the max number walls on grid */
            if (wallQueue.size() > 0 && wallCount.get() < MAX_TOTAL_WALLS) {
                game.queueWall(); // queue wall
            }
        }, 0, BUILD_DELAY, TimeUnit.MILLISECONDS); // schedule to run every 2 seconds
    }

    public Wall getNextWallinQueue() {
        return wallQueue.poll();
    }

    public AtomicInteger getWallCount() {
        return wallCount;
    }

    public void addToQueue(Wall w) {
        try {
            wallsQueued.incrementAndGet();
            wallQueue.put(w);
        } catch (InterruptedException e) {
            System.out.println("WallScheduler: Interrupted while adding wall to queue." + e.getMessage());
        }
    }

    /* Checks if that location is already in queue */
    public boolean isAlreadyInQueue(int x, int y) {
        for (Wall w : wallQueue) {
            if (w.getX() == x && w.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public int getQueueCount() {
        return wallQueue.size();
    }

    public int maxQueueSize() {
        return MAX_QUEUE_SIZE;
    }

    public int getMaxTotalWalls() {
        return MAX_TOTAL_WALLS;
    }
}
