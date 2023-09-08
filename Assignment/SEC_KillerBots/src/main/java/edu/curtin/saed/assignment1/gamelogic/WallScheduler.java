package edu.curtin.saed.assignment1.gamelogic;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import edu.curtin.saed.assignment1.Grid;
import edu.curtin.saed.assignment1.gridobjects.Wall;

public class WallScheduler {
    /* Class constants */
    private static final int BUILD_DELAY = 2000; // How long thread has to wait to build wall in milliseconds
    /* Class variables */
    private Grid grid;
    private Game game;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // limits to 1 thread
    private BlockingQueue<Wall> wallQueue = new ArrayBlockingQueue<>(5); // max 5 walls can be queued
    private AtomicInteger wallCount = new AtomicInteger(0);

    public WallScheduler(Grid inGrid, Game inGame) {
        grid = inGrid;
        game = inGame;
        initWallScheduler();
    }

    /**
     * Creates a scheduler to add walls to the grid at regular intervals
     */
    private void initWallScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            synchronized (grid) {
                if (wallQueue.size() > 0 && wallCount.get() < 10) {
                    Wall w = wallQueue.poll();
                    if (grid.isCellEmpty(w.getX(), w.getY())) {
                        game.buildWall(w);
                        wallCount.incrementAndGet();
                    }
                }
            }
        }, 0, BUILD_DELAY, TimeUnit.MILLISECONDS);
    }

    public void addToQueue(Wall w) {
        try {
            wallQueue.put(w);
        } catch (InterruptedException e) {
            System.out.println("WallScheduler: Interrupted while adding wall to queue." + e.getMessage());
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public int getQueueCount() {
        return wallQueue.size();
    }
}
