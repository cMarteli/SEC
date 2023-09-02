package edu.curtin.saed.assignment1;

import edu.curtin.saed.assignment1.gridObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.awt.Point;


/* Game class to manage game logic
 * TODO: FIX MEMORY LEAKS, ONLY WORKS WITH ONE BOT!!!!
*/
public class Game {

    private Grid grid;
    private Point citadel;
    JFXArena arena;

    private volatile boolean running = true;  // New flag to control threads
    private final List<Thread> activeThreads = new CopyOnWriteArrayList<>(); // New list to hold active threads


    Bot b1, b2; //TODO: DEBUG ONLY

    public Game(JFXArena a) {
        arena = a;
        grid = new Grid(9, 9); //TODO: Hardcoded values;
        citadel = grid.getCitadelLocation();
        b1 = new Bot(0, 0); //TODO: DEBUG ONLY
        b2 = new Bot(0, 2); //TODO: DEBUG ONLY

        System.out.println("Game created");//DEBUG
    }

    // public void initGame(){
    //     System.out.println("Game started");//DEBUG

    //     new Thread(() -> {
    //         try {
    //             initiateRobotMovement(b1); //TODO: DEBUG ONLY
    //         } catch (InterruptedException e) {
    //             // Handle exception
    //         }
    //     }).start();


    // }

    public void initGame(){
        System.out.println("Game started");//DEBUG

        /* Create a new thread and add it to the activeThreads list */
        Thread t1 = new Thread(() -> {
            try {
                initiateRobotMovement(b1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Reset the interrupt flag
            }
        });

        activeThreads.add(t1);
        t1.start();
    }

    public void stopGame() {
        // Stop all active threads
        running = false; // Stop the threads
        for (Thread t : activeThreads) {
            t.interrupt();  // Interrupt the thread
        }
        activeThreads.clear();  // Remove all threads from the list
    }



    /**
     * Updates the graphical representation on the JFXArena.
     */
    public void updateScreen() {
        // Assuming your Grid object is a 2D array representation of the grid
        GridObject[][] gridArray = grid.getGrid();

        // Loop through each cell in the grid
        for (int y = 0; y < gridArray.length; y++) {
            for (int x = 0; x < gridArray[0].length; x++) {
                GridObject obj = gridArray[y][x];
                // Check if the grid cell contains a robot
                if (obj instanceof Bot) {
                    arena.setRobotPosition(x, y);
                }
                // Check for other types of GridObjects and update the arena accordingly
            }
        }

        // Optionally: Set the citadel position in the arena based on your game logic
        //Point citadel = grid.getCitadelPosition();  // Replace with your method to get citadel position
        //arena.setCitadelPosition(citadel.getX(), citadel.getY());

        // Force a layout update on the arena
        arena.requestLayout();
    }

   public void decideNextMove(Bot robot) throws InterruptedException {
        ArrayList<Point> possibleMoves = new ArrayList<>();

        // Check Up
        Point up = new Point(robot.getX(), robot.getY() - 1);
        if (robot.getY() > 0 && this.grid.isCellEmpty(up)) { // if cell is empty and within boundaries
            possibleMoves.add(up); //add to possible moves list
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
        grid.getGrid()[robot.getY()][robot.getX()] = null;  // Empty current cell
        grid.getGrid()[newCoords.y][newCoords.x] = robot;  // Move to new cell
        robot.move(newCoords); //move robot to new point
    }

    // Function to initiate robot movements
    //TODO: Should take a list of bots not just one
    public void initiateRobotMovement(Bot b) throws InterruptedException {
        System.out.println("Bot started moving"); //debug
        while (running && !Thread.currentThread().isInterrupted()){
            this.decideNextMove(b);
            TimeUnit.MILLISECONDS.sleep(b.getDelayValue());
        }
    }


}
