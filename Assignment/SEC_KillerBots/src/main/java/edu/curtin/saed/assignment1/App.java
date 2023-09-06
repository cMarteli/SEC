/**
 * App.java
 * Main class of KillerBots Game
 * Some java code adapted from:
 * Software Engineering Concepts JavaFXDemo by David Cooper
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1;

import java.util.concurrent.atomic.AtomicInteger;

import edu.curtin.saed.assignment1.JFX.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application implements BotSpawnListener {// TODO: add the listener to arena class?

    public static final String APP_TITLE = "Revenge of the Killer Bots";
    public static final int DIMENSIONX = 9; // TODO: Hardcoded
    public static final int DIMENSIONY = 9; // TODO: Hardcoded
    private TextArea logger;
    private AtomicInteger score = new AtomicInteger(0); // Atomic to ensure thread safety
    private Label scoreLbl; // Label to display score
    private boolean gameRunning = true; // Flag to keep track of the game status
    Game game;
    Grid grid;

    // Create a new AnimationTimer
    AnimationTimer timer = new AnimationTimer() {
        private long lastUpdate = 0;
        private long lastScoreUpdate = 0;

        @Override
        public void handle(long nowNano) {
            long now = nowNano / 1_000_000; // Convert to milliseconds
            if (gameRunning && now - lastUpdate >= 40) { // 25fps is every 40 ms
                game.updateScreen();
                lastUpdate = now;
            }
            if (gameRunning && now - lastScoreUpdate >= 1000) { // 1s is 1000 ms
                int newScore = score.addAndGet(10); // Increase score by 10
                scoreLbl.setText("Score: " + newScore);
                lastScoreUpdate = now;
            }
        }
    };

    public static void main(String[] args) {
        launch();
    }

    /** Frees memory when game is closed */
    @Override
    public void stop() {
        System.out.println("Closing game");// DEBUG
        gameRunning = false; // Stop the game timer
        game.stopGame(); // Stop the game when the application stops
    }

    @Override
    public void start(Stage stage) // stage is passed in as a parameter
    {
        Image icon = new Image(Graphics.ICON_IMAGE);
        stage.getIcons().add(icon); // sets tray icon
        stage.setTitle(APP_TITLE); // sets title of window
        JFXArena arena = new JFXArena(); // creates arena object
        logger = new TextArea();

        grid = new Grid(DIMENSIONX, DIMENSIONY); // creates grid object
        game = new Game(arena, grid); // Creates a new game instance

        ToolBar toolbar = new ToolBar();
        Button fsBtn = new Button("Fullscreen");
        Button endBtn = new Button("Stop Game");
        scoreLbl = new Label("Score: 0"); // Initialize the score label
        Label queue = new Label("Walls Queued");
        toolbar.getItems().addAll(scoreLbl, new Separator(), queue, new Separator(),
                fsBtn, new Separator(), endBtn);

        /* Buttons */
        // Fullscreen button listener
        fsBtn.setOnAction((event) -> {
            toggleFullscreen(stage);
        });
        // ends game
        endBtn.setOnAction((event) -> {
            stop();
            logger.appendText("\nEnd of game\n Final Score:" + score.get() + "\n");
            // Platform.exit();
        });

        arena.addListener((x, y) -> {
            logger.appendText("\nArena click at (" + x + "," + y + ")");
        });
        arena.setBotSpawnListener(this);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);

        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        // Scene scene = new Scene(contentPane, SCENE_WIDTH, SCENE_HEIGHT);
        Scene scene = new Scene(contentPane, (DIMENSIONX - 1) * 100, (DIMENSIONY - 1) * 100);
        stage.setScene(scene);
        stage.show();

        game.initGame(); // starts game here

        /* Auto refresh the screen */

        // Start the AnimationTimer
        timer.start();
    }

    private void toggleFullscreen(Stage stage) {
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
        } else {
            stage.setFullScreen(true);
        }
    }

    @Override
    public void onBotSpawn(int x, int y) {
        logger.appendText("\nNew bot spawned at (" + x + "," + y + ")");
    }

}
